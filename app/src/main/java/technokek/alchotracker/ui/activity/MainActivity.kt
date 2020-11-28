package technokek.alchotracker.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import technokek.alchotracker.R
import technokek.alchotracker.ui.fragments.FriendProfileFragment
import technokek.alchotracker.ui.fragments.MasterProfileFragment


class MainActivity : AppCompatActivity() {
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

        if (supportFragmentManager.findFragmentByTag(MasterProfileFragment.TAG) == null)
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, FriendProfileFragment("0oyvKHqUx5hxILqeSmkCYOwj6wv2"))
                .commit()
    }

}