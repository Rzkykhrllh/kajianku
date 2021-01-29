package com.purplepotato.kajianku.core.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.purplepotato.kajianku.R

object Notifier {
    private const val CHANNEL_ID = "channel_1"
    private const val CHANNEL_NAME = "Reminder Kajian"

    fun postReminderNotification(context: Context, id: Long, text: String) {
        val mNotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_active)
            .setContentTitle(context.getString(R.string.notification_title_reminder))
            .setContentText(text)
            .setAutoCancel(true)
            .setCategory(Notification.CATEGORY_REMINDER)
            .setStyle(NotificationCompat.BigTextStyle().bigText(text))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )

            builder.setChannelId(CHANNEL_ID)

            mNotificationManager.createNotificationChannel(notificationChannel)
        }

        val notification = builder.build()

        mNotificationManager.notify(id.toInt(), notification)
    }
}