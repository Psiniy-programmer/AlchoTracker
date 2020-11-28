package technokek.alchotracker.ui.activity

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
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

    private var mFriendFragment: FriendFragment? = null
    private var mEventFragment: EventFragment? = null
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance();
        mAuth!!.signInWithEmailAndPassword("denislipoff@yandex.ru", "ssssss")
            .addOnCompleteListener(this,
                OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("OKZ", mAuth!!.currentUser?.uid.toString())
                    } else {
                        Log.d("kek", "signInWithEmail:failure", task.exception)
                    }
                })

        mFriendFragment =
            supportFragmentManager.findFragmentByTag(FriendFragment.TAG) as FriendFragment?
        mEventFragment =
            supportFragmentManager.findFragmentByTag(EventFragment.TAG) as EventFragment?

        if (mFriendFragment == null)
            mFriendFragment = FriendFragment()
        if (mEventFragment == null)
            mEventFragment = EventFragment()

        val friendButton = findViewById<Button>(R.id.friend_button)
        friendButton.setOnClickListener {
            showFriendFragment()
        }
        val eventButton = findViewById<Button>(R.id.event_button)
        eventButton.setOnClickListener {
            showEventFragment()
        }

        if (savedInstanceState == null) {
            showFriendFragment()
        }
    }

    private fun showFriendFragment() {
        supportFragmentManager.findFragmentByTag(FriendFragment.TAG)?.let {
            return
        }

        supportFragmentManager.findFragmentByTag(EventFragment.TAG)?.let {
            supportFragmentManager.beginTransaction()
                .remove(supportFragmentManager.findFragmentByTag(EventFragment.TAG)!!)
                .commit()
        }

        supportFragmentManager.beginTransaction()
            .add(R.id.content, mFriendFragment!!, FriendFragment.TAG)
            .commit()
    }

    private fun showEventFragment() {
        supportFragmentManager.findFragmentByTag(EventFragment.TAG)?.let {
            return
        }

        supportFragmentManager.findFragmentByTag(FriendFragment.TAG)?.let {
            supportFragmentManager.beginTransaction()
                .remove(supportFragmentManager.findFragmentByTag(FriendFragment.TAG)!!)
                .commit()
        }

        supportFragmentManager.beginTransaction()
            .add(R.id.content, mEventFragment!!, EventFragment.TAG)
            .commit()
    }

    override fun pressEvent() {
        val toast = Toast.makeText(this, "EventList", Toast.LENGTH_SHORT)
        toast.show()
    }

    override fun pressFriend() {
        val toast = Toast.makeText(this, "FriendList", Toast.LENGTH_SHORT)
        toast.show()
    }
}
