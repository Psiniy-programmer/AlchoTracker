package technokek.alchotracker.ui.activity

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import technokek.alchotracker.R
import technokek.alchotracker.api.EventClickListener
import technokek.alchotracker.api.FriendClickListener
import technokek.alchotracker.ui.fragments.EventFragment
import technokek.alchotracker.ui.fragments.FriendFragment
import technokek.alchotracker.ui.fragments.FriendProfileFragment
import technokek.alchotracker.ui.fragments.MasterProfileFragment


class MainActivity : AppCompatActivity(), EventClickListener, FriendClickListener {

    private var mAuth: FirebaseAuth? = null
    private lateinit var bottomNavigationView: BottomNavigationView

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

    fun setUpNavigation() {
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        val navHostFragment: NavHostFragment = supportFragmentManager
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

        val mFriendProfileFragment = FriendProfileFragment(uid)

        supportFragmentManager.beginTransaction()
            .replace(R.id.content, mFriendProfileFragment, FriendProfileFragment.TAG)
            .commit()
    }
}
