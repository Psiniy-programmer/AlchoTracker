package technokek.alchotracker.ui.activity

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import technokek.alchotracker.R
import technokek.alchotracker.api.EventClickListener
import technokek.alchotracker.api.FriendClickListener
import technokek.alchotracker.api.NotificationMethodsHolder
import technokek.alchotracker.api.SharedPreferencesHolder
import technokek.alchotracker.data.Constants
import technokek.alchotracker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), EventClickListener, FriendClickListener, SharedPreferencesHolder {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navHostFragment: NavHostFragment
    internal lateinit var binding: ActivityMainBinding
    override lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE)!!

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

        navHostFragment.navController.navigate(
            R.id.action_friendFragment_to_friendProfileFragment,
            bundle
        )
    }



}
