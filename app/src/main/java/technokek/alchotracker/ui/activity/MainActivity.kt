package technokek.alchotracker.ui.activity

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import android.util.Log
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import technokek.alchotracker.R
import technokek.alchotracker.calendar.CalendarFragment
import technokek.alchotracker.databinding.ActivityMainBinding
import technokek.alchotracker.timer.NotificationActivity
import technokek.alchotracker.ui.fragments.TimerFragment
import technokek.alchotracker.ui.fragments.Profile
import technokek.alchotracker.api.EventClickListener
import technokek.alchotracker.api.FriendClickListener
import technokek.alchotracker.ui.fragments.EventFragment
import technokek.alchotracker.ui.fragments.FriendFragment
import technokek.alchotracker.ui.fragments.FriendProfileFragment
import technokek.alchotracker.ui.fragments.MasterProfileFragment


class MainActivity : AppCompatActivity(), EventClickListener, FriendClickListener {

    private var mAuth: FirebaseAuth? = null
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navHostFragment: NavHostFragment
    private val fragmentManager = supportFragmentManager
    private lateinit var notificationManager: NotificationManagerCompat
    internal lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance();
        mAuth!!.signInWithEmailAndPassword("denislipoff@yandex.ru", "ssssss")
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("OKZ", mAuth!!.currentUser?.uid.toString())
                } else {
                    Log.d("kek", "signInWithEmail:failure", task.exception)
                }
            }

        setUpNavigation()
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        binding = ActivityMainBinding.inflate(layoutInflater)
    }

    private fun setUpNavigation() {
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        navHostFragment = supportFragmentManager
            .findFragmentById(R.id.content) as NavHostFragment
        NavigationUI.setupWithNavController(bottomNavigationView, navHostFragment.navController)
    }

    override fun pressEvent() {
        val toast = Toast.makeText(this, "EventList", Toast.LENGTH_SHORT)
        toast.show()
    }

    override fun pressFriend(uid: String) {
        val toast = Toast.makeText(this, "FriendList", Toast.LENGTH_SHORT)
        toast.show()

        val bundle = Bundle()
        bundle.putString("uid", uid)

        navHostFragment.navController.navigate(R.id.action_friendFragment_to_friendProfileFragment, bundle)
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


    private fun addExampleFragment() {
        val calendarFragment = CalendarFragment()
        val transaction = fragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, calendarFragment)
            .addToBackStack(null)
            .commit()
    }
}
