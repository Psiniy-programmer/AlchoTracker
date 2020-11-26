package technokek.alchotracker.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import technokek.alchotracker.R
import technokek.alchotracker.ui.fragments.Profile

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragment_container, Profile("1"))
        fragmentTransaction.commit()
    }
}