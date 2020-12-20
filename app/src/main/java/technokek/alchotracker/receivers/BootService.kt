package technokek.alchotracker.receivers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import technokek.alchotracker.data.Constants.*
import java.time.LocalDateTime
import java.util.*

class BootService : JobIntentService() {
    companion object {
        const val JOB_ID = 1000

        fun enqueWork(context: Context, work:Intent) {
            enqueueWork(context, BootService::class.java, JOB_ID, work)
            Log.d("BootService", "Works")
        }

        fun getCalendarAndStartAlarm(timeStart: LocalDateTime): Calendar {
            val calendar = Calendar.getInstance()
            calendar.set(
                timeStart.year,
                timeStart.monthValue - 1,
                timeStart.dayOfMonth,
                timeStart.hour,
                timeStart.minute
            )
            calendar.set(Calendar.SECOND, 0)
            return calendar
        }
    }
    override fun onHandleWork(intent: Intent) {
        val sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        if (sharedPreferences.contains(TIMER_TIMESTAMPS_IN_SP)) {
            Log.d("BootService", "Contains timestamps")
        }
        else {
            Log.d("BootService", "NOT contains timestamps")
        }
        val timestamps = sharedPreferences.getStringSet(TIMER_TIMESTAMPS_IN_SP, mutableSetOf("2020-12-23T06:00;59968"))
        Log.d("BootServiceTimestamps", timestamps.toString())
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        CoroutineScope(Dispatchers.IO).launch {
            timestamps?.forEach {
                val parts = it.split(";")
                startAlarm(LocalDateTime.parse(parts[0]), parts[1].toInt(), alarmManager)
            }
        }
    }

    private fun startAlarm(timeStart: LocalDateTime, requestCode: Int, alarmManager: AlarmManager) {
        val calendar = getCalendarAndStartAlarm(timeStart)
        val intent: Intent = Intent(this, AlertReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            requestCode,
            intent,
            PendingIntent.FLAG_ONE_SHOT
        )
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }
}