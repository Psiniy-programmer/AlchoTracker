package technokek.alchotracker.ui.activity

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import technokek.alchotracker.R
import technokek.alchotracker.api.*
import technokek.alchotracker.data.Constants
import technokek.alchotracker.data.models.ChatFriendModel
import technokek.alchotracker.data.models.FriendModel
import technokek.alchotracker.databinding.ActivityMainBinding


class MainActivity :
    AppCompatActivity(), EventClickListener,
    FriendClickListener, FoundUserListener,
    ChatListListener, SharedPreferencesHolder {

    lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var binding: ActivityMainBinding
    override lateinit var sharedPreferences: SharedPreferences
    private lateinit var notificationManager: NotificationManagerCompat
    lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE)!!

        // Mandatory for notifications!!!
        notificationManager = NotificationManagerCompat.from(this)

        setUpNavigation()
//        getTokenInLog()
    }

    private fun getTokenInLog() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("NEW_TOKEN_MAIN", "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                val token = task.result

                // Log and toast
                val msg = token.toString()
                Log.d("NEW_TOKEN_MAIN", msg)
                //Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
            })
    }

//    private fun checkKeyBoardUp() {
//        bottomNavigationView
//            .viewTreeObserver
//            .addOnGlobalLayoutListener {
//                val r: Rect = Rect()
//                bottomNavigationView.getWindowVisibleDisplayFrame(r)
//                val heightDiff = bottomNavigationView
//            }
//    }

//    public void checkKeyBoardUp(){
//        bottomNavigationView.getViewTreeObserver()
//            .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                @Override public void onGlobalLayout() {
//                    Rect r = new Rect();
//                    rootView.getWindowVisibleDisplayFrame(r);
//                    int heightDiff = rootView.getRootView().getHeight() - (r.bottom - r.top);
//                    if (heightDiff > 100) {
//                        whatEverView.setVisibility(View.INVISIBLE);
//                    } else { //ok now we know the keyboard is down...
//                        whatEverView.setVisibility(View.VISIBLE); }
//                }
//            });
//    }

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

    override fun pressUser(uid: String) {
        val toast = Toast.makeText(this, "FoundUser", Toast.LENGTH_SHORT)
        toast.show()

        if (uid == FirebaseAuth.getInstance().uid) {
            navHostFragment.navController.navigate(
                R.id.action_friendFragment_to_masterProfileFragment
            )
        } else {
            val bundle = Bundle()
            bundle.putString("uid", uid)

            navHostFragment.navController.navigate(
                R.id.action_friendFragment_to_friendProfileFragment,
                bundle
            )
        }
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

    override fun pressChat(chatID: String, model: FriendModel) {
        val toast = Toast.makeText(this, "FriendList", Toast.LENGTH_SHORT)
        toast.show()

        val bundle = Bundle()
        bundle.putString("chatID", chatID)
        bundle.putString("name", model.name)
        bundle.putString("avatar", model.avatar)
        bundle.putString("uid", model.id)
        navHostFragment.navController.navigate(
            R.id.action_chatListFragment_to_chatFragment,
            bundle
        )
    }

    override fun pressChatFriend(chatID: String, model: ChatFriendModel) {
        val toast = Toast.makeText(this, "FriendList", Toast.LENGTH_SHORT)
        toast.show()

        val bundle = Bundle()
        bundle.putString("chatID", chatID)
        bundle.putString("avatar", model.avatar)
        bundle.putString("name", model.name)
        bundle.putString("uid", model.friendID)

        Log.d("SUKA", "PRESSCHATFRIEND: $chatID, $model")

        navHostFragment.navController.navigate(
            R.id.action_chatListFragment_to_chatFragment,
            bundle
        )
    }
}
