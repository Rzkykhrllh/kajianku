package com.purplepotato.kajianku.core.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        private const val EXTRA_ID = "extra_id"
        private const val EXTRA_MESSAGE = "extra_message"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            val id = intent.getLongExtra(EXTRA_ID, 0)
            val message = intent.getStringExtra(EXTRA_MESSAGE)
            Notifier.postReminderNotification(
                context as Context,
                id,
                message as String
            )
        }
    }

    fun setOneTimeAlarm(
        context: Context,
        requestCode: Int,
        id: Long,
        message: String,
        triggerAtMillis: Long
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(EXTRA_ID, id)
        intent.putExtra(EXTRA_MESSAGE, message)

        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
    }

    fun cancelAlarm(context: Context, requestCode: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        pendingIntent.cancel()

        alarmManager.cancel(pendingIntent)
    }
}