package com.example.noteskeeping.view

import android.app.*
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.example.noteskeeping.R
import com.example.noteskeeping.databinding.FragmentReminderBinding
import com.example.noteskeeping.model.AlarmRecevier
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.util.*


class ReminderFragment : Fragment() {
    lateinit var binding: FragmentReminderBinding
    private lateinit var picker: MaterialTimePicker
    private lateinit var datePicker: MaterialDatePicker<String>
    private lateinit var  calendar: Calendar
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent
    // private lateinit var alarmRecevier: AlarmRecevier

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reminder, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentReminderBinding.bind(view)
        val dateView: TextView = binding.dateView
//        val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().time)
//        dateView.text = SimpleDateFormat("dd.MM.yyyy HH.mm.ss").format(System.currentTimeMillis())
//        dateView.text = date
//        val timeView: TextView = binding.dateView
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            timeView.text = LocalTime.now().toString()
//        }

        createNotificationChannel()

        binding.selectButton.setOnClickListener{
            showtimePicker()
        }

        binding.cancelAlarm.setOnClickListener {
            cancelAlarm()
        }

        binding.setAlarm.setOnClickListener {
            setAlarm()
        }
    }

    private fun cancelAlarm() {
        alarmManager = activity?.getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(),AlarmRecevier::class.java)
        pendingIntent = PendingIntent.getBroadcast(requireContext(),0,intent,0)
        alarmManager.cancel(pendingIntent)
        Toast.makeText(context,"Alarm Cancelled",Toast.LENGTH_LONG).show()
    }

    private fun setAlarm() {
        alarmManager = activity?.getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(),AlarmRecevier::class.java)
        pendingIntent = PendingIntent.getBroadcast(requireContext(),0,intent,0)
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,pendingIntent
        )
        Toast.makeText(context,"Alarm set Successfully", Toast.LENGTH_SHORT).show()
    }

    private fun showtimePicker() {
        calendar = Calendar.getInstance()
        var YEAR = calendar.get(Calendar.YEAR)
        var MONTH = calendar.get(Calendar.MONTH)
        var DAY = calendar.get(Calendar.DAY_OF_MONTH)

        val textView: TextView = binding.dateView
        textView.text = SimpleDateFormat("dd.MM.yyyy").format(System.currentTimeMillis())

        var cal = Calendar.getInstance()

        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd.MM.yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.UK)
            textView.text = sdf.format(cal.time)
            picker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Select Alarm Time")
                .build()

            picker.show(requireFragmentManager(),"notekeeping")

            picker.addOnPositiveButtonClickListener{

                if(picker.hour > 12){
                    binding.timeView.text =
                        String.format("%02d",picker.hour - 12)+" : " + String.format("%02d",picker.minute) + "PM"
                }else{
                    String.format("%02d",picker.hour)+" : " + String.format("%02d",picker.minute) + "AM"
                }
//                calendar = Calendar.getInstance()
                calendar[Calendar.HOUR_OF_DAY] = picker.hour
                calendar[Calendar.MINUTE] = picker.minute
                calendar[Calendar.SECOND] = 0
                calendar[Calendar.MILLISECOND] = 0
            }

        }

        textView.setOnClickListener {
            DatePickerDialog(requireActivity(), dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }

       //var dataPickerDialog = DatePickerDialog(co)

//        picker = MaterialTimePicker.Builder()
//            .setTimeFormat(TimeFormat.CLOCK_12H)
//            .setHour(12)
//            .setMinute(0)
//            .setTitleText("Select Alarm Time")
//            .build()
//
//        picker.show(requireFragmentManager(),"notekeeping")
//
//        picker.addOnPositiveButtonClickListener{
//
//            if(picker.hour > 12){
//                binding.timeView.text =
//                    String.format("%02d",picker.hour - 12)+" : " + String.format("%02d",picker.minute) + "PM"
//            }else{
//                String.format("%02d",picker.hour)+" : " + String.format("%02d",picker.minute) + "AM"
//            }
//            calendar = Calendar.getInstance()
//            calendar[Calendar.HOUR_OF_DAY] = picker.hour
//            calendar[Calendar.MINUTE] = picker.minute
//            calendar[Calendar.SECOND] = 0
//            calendar[Calendar.MILLISECOND] = 0
//        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "NoteKeeping Reminder"
            val description = "More to do on NoteKeeping"
            val important = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("notekeeping", name, important)
            channel.description = description
            val notificationManager = getSystemService(requireContext(),NotificationManager::class.java)

            notificationManager?.createNotificationChannel(channel)
        }
    }

    fun Fragment.vibratePhone() {
        val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(200)
        }
    }
}