package com.example.noteskeeping

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.noteskeeping.view.HomeActivity
import com.example.noteskeeping.view.NoteFragment
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val channelId = "notification_channel"
const val channelName = "com.example.noteskeeping.view"
class MyFirebaseMessagingService : FirebaseMessagingService(){

    fun generationNotification(title : String,message : String){
        val intent = Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)

        val pendingIntent = PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_ONE_SHOT)
        var builder: NotificationCompat.Builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.google_logo)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000,1000,1000,1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        builder = builder.setContent(getRemoteView(title,message))

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(0,builder.build())
    }

    fun getRemoteView(title : String, message : String): RemoteViews? {
        val remoteView = RemoteViews("com.example.noteskeeping",R.layout.push_notification)
        remoteView.setTextViewText(R.id.notification_title,title)
        remoteView.setTextViewText(R.id.notification_description,message)
        remoteView.setImageViewResource(R.id.app_profile,R.drawable.google_logo)
        return  remoteView
    }

    override fun onMessageReceived(message: RemoteMessage) {

        if(message.notification != null){
            generationNotification(message.notification!!.title!!, message.notification!!.body!!)
        }
    }
}
