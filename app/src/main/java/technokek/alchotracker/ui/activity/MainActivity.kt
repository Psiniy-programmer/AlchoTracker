package technokek.alchotracker.ui.activity

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import technokek.alchotracker.R
import technokek.alchotracker.api.EventClickListener
import technokek.alchotracker.api.FriendClickListener
import technokek.alchotracker.ui.fragments.EventFragment
import technokek.alchotracker.ui.fragments.FriendFragment

class MainActivity : AppCompatActivity(), EventClickListener, FriendClickListener {

    private var mFriendFragment: FriendFragment? = null
    private var mEventFragment: EventFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
