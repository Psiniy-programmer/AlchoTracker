package technokek.alchotracker

import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.FragmentManager
import technokek.alchotracker.timer.NotificationActivity
import technokek.alchotracker.timer.TimerFragment

class MainActivity : AppCompatActivity() {
    private val fragmentManager = supportFragmentManager
    private lateinit var notificationManager: NotificationManagerCompat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        notificationManager =
            NotificationManagerCompat.from(this)
        addTimerFragment()
    }

    private fun addTimerFragment() {
        val timerFragment = TimerFragment()
        val transaction = fragmentManager
            .beginTransaction()
            .replace(R.id.content_parent, timerFragment)
            .addToBackStack(null)
            .commit()
    }

    fun sendTimerFragmentNotificationChannel1(id:Int, title:String, text:String) {
        val builder = NotificationCompat.Builder(this, NotificationActivity.CHANNEL_1_ID)
            .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
            .setContentTitle(title)
            .setContentText(text)
        val notification = builder.build()
        notificationManager.notify(id, notification)
    }

    fun sendTimerFragmentNotificationChannel2(id:Int, title:String, text:String) {
        val builder = NotificationCompat.Builder(this, NotificationActivity.CHANNEL_2_ID)
            .setSmallIcon(R.drawable.ic_notification_time_interval)
            .setContentTitle(title)
            .setContentText(text)
        val notification = builder.build()
        notificationManager.notify(id, notification)
    }
}
