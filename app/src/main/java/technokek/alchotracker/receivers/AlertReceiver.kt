package technokek.alchotracker.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import technokek.alchotracker.R
import technokek.alchotracker.api.NotificationMethodsHolder
import technokek.alchotracker.data.Constants
import technokek.alchotracker.ui.activity.NotificationActivity
import technokek.alchotracker.data.Constants.*
import java.time.LocalDateTime

class AlertReceiver : BroadcastReceiver(), NotificationMethodsHolder {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("ContextInAlarm", context.toString())
        val notificationManager = context?.let { NotificationManagerCompat.from(it) }
        sendTimerFragmentNotificationChannel1(
            context = context!!,
            notificationManager = notificationManager!!,
            id = Constants.START_NOTIFICATION_ID,
            title = Constants.START_NOTIFICATION_TITLE,
            text = Constants.START_NOTIFICATION_TEXT
        )
        CoroutineScope(Dispatchers.IO).launch {
            deleteOldTimestampsFromSP(context)
        }
    }

    override fun sendTimerFragmentNotificationChannel1(context: Context, notificationManager: NotificationManagerCompat,
                                                       id: Int, title: String, text: String) {
        val builder = NotificationCompat.Builder(context, NotificationActivity.CHANNEL_1_ID)
            .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
            .setContentTitle(title)
            .setContentText(text)
        val notification = builder.build()
        notificationManager.notify(id, notification)
        Log.d("Notification1", "Im here")
    }

    override fun sendTimerFragmentNotificationChannel2(context: Context, notificationManager: NotificationManagerCompat,
                                                       id: Int, title: String, text: String) {
        val builder = NotificationCompat.Builder(context, NotificationActivity.CHANNEL_2_ID)
            .setSmallIcon(R.drawable.ic_notification_time_interval)
            .setContentTitle(title)
            .setContentText(text)
        val notification = builder.build()
        notificationManager.notify(id, notification)
    }

    private fun deleteOldTimestampsFromSP(context: Context) {
        val sp = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        val timestamps = sp.getStringSet(TIMER_TIMESTAMPS_IN_SP, mutableSetOf())
        val now = LocalDateTime.now()
        timestamps!!.removeIf {
            localDateTimeFromTimestamp(it).isBefore(now)
        }
        val editor = sp.edit()
        editor.clear()
        editor.putStringSet(TIMER_TIMESTAMPS_IN_SP, timestamps).apply()
    }

    companion object {
        fun localDateTimeFromTimestamp(timestamp: String): LocalDateTime {
            return LocalDateTime.parse(timestamp.split(";")[0])
        }
    }

}