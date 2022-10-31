package com.example.noteskeeping.view

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.getSystemServiceName
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteskeeping.R
import com.example.noteskeeping.database.DataBaseHelper
import com.example.noteskeeping.databinding.FragmentDialogBinding
import com.example.noteskeeping.databinding.FragmentNoteBinding
import com.example.noteskeeping.databinding.FragmentReminderBinding
import com.example.noteskeeping.model.AlarmRecevier
import com.example.noteskeeping.model.NoteServices
import com.example.noteskeeping.model.Notes
import com.example.noteskeeping.viewModel.NotesViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class ReminderFragment : Fragment() {
    lateinit var binding: FragmentReminderBinding
    private lateinit var picker: MaterialTimePicker
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
            calendar = Calendar.getInstance()
            calendar[Calendar.HOUR_OF_DAY] = picker.hour
            calendar[Calendar.MINUTE] = picker.minute
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0
        }
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
}