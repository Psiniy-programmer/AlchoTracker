package technokek.alchotracker.ui.activity

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import technokek.alchotracker.R
import technokek.alchotracker.api.EventClickListener
import technokek.alchotracker.ui.fragments.EventFragment

class MainActivity : AppCompatActivity(), EventClickListener {

    private var mEventFragment: EventFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        mEventFragment =
            supportFragmentManager.findFragmentByTag(EventFragment.TAG) as EventFragment?

        if (mEventFragment == null)
            mEventFragment = EventFragment()

        val eventButton = findViewById<Button>(R.id.event_button)
        eventButton.setOnClickListener {
            showEventFragment()
        }

        if (savedInstanceState == null) {
            showEventFragment()
        }
    }

    private fun showEventFragment() {
        supportFragmentManager.findFragmentByTag(EventFragment.TAG)?.let {
            return
        }

        supportFragmentManager.beginTransaction()
            .add(R.id.content, mEventFragment!!, EventFragment.TAG)
            .commit()
    }

    override fun pressEvent() {
        val toast = Toast.makeText(this, "Event", Toast.LENGTH_SHORT)
        toast.show()
    }

}