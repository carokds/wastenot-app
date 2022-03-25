package com.hoang.wastenot.fragments


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.hoang.wastenot.R

internal const val CHANNEL_ID = "UseIngredients"
const val notificationId = 100
private const val titleText = "Your ingredient is expiring soon"
private const val messageText = "Check suggested recipes"


class Notifications : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val intent = Intent(context, FoodDetailFragment::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK


        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.bell)
            .setContentTitle(titleText)
            .setContentText(messageText)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setAutoCancel(true)
            .build()

        createNotificationChannel(context)

        val manager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationId, builder)


    }

    private fun createNotificationChannel(context: Context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification"
            val description = "Receive reminders to use ingredients"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance)

            (context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(
                channel
            )

        }
    }
}













