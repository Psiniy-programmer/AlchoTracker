package technokek.alchotracker.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import technokek.alchotracker.R
import technokek.alchotracker.timer.NotificationActivity
import technokek.alchotracker.ui.fragments.TimerFragment
import technokek.alchotracker.ui.fragments.Profile

class MainActivity : AppCompatActivity() {
    private val fragmentManager = supportFragmentManager
    private lateinit var notificationManager: NotificationManagerCompat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment = Profile();
        fragmentTransaction.add(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
        notificationManager =
            NotificationManagerCompat.from(this)
        addTimerFragment()
    }

    private fun addTimerFragment() {
        val timerFragment = TimerFragment()
        val transaction = fragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, timerFragment)
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
