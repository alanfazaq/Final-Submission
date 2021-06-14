package com.dicoding.consumerapps.Notification

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.dicoding.consumerapps.R
import com.dicoding.consumerapps.View.MainActivity
import java.util.*

class ReceiverAlarm : BroadcastReceiver() {

    companion object {
        const val TYPE_Remind = "Daily Reminder"
        const val Notify_Message = "message"
        const val KEY_TYPE = "key_type"

        private const val Id_Reminder = 100
        private const val TIME_DAILY = "09:00"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val message = intent.getStringExtra(Notify_Message)
        showNotifyDaily(context, message)
    }

    fun setDailyReminder(context: Context, type: String, message: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReceiverAlarm::class.java)
        val timeArray =
            TIME_DAILY.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val calendarDayly = Calendar.getInstance()
        calendarDayly.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]))
        calendarDayly.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
        calendarDayly.set(Calendar.SECOND, 0)

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra(Notify_Message, message)
        intent.putExtra(KEY_TYPE, type)

        val pendingAlarm = PendingIntent.getBroadcast(
            context,
            Id_Reminder, intent, PendingIntent.FLAG_ONE_SHOT
        )
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendarDayly.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingAlarm
        )
        Toast.makeText(context, context.getString(R.string.alarm_on), Toast.LENGTH_SHORT).show()
    }

    private fun showNotifyDaily(context: Context, message: String?) {
        val nameId = R.string.github.toString()
        val titleName = R.string.daily_reminder.toString()

        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val soundAlarm = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notifyManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val build = NotificationCompat.Builder(context, nameId)
            .setSmallIcon(R.drawable.ic_baseline_alarm_24)
            .setContentTitle(R.string.daily_message.toString())
            .setContentText(message)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setSound(soundAlarm)
            .setVibrate(longArrayOf(1500, 1500))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                nameId, titleName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1500, 1500)
            build.setChannelId(nameId)
            notifyManager.createNotificationChannel(channel)
        }
        val notify = build.build()
        notifyManager.notify(100, notify)
    }

    fun cancelAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReceiverAlarm::class.java)
        val requestCode = Id_Reminder
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
        Toast.makeText(context, context.getString(R.string.alarm_off), Toast.LENGTH_SHORT).show()
    }
}