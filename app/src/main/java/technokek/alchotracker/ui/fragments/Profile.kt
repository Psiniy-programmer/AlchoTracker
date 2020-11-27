package technokek.alchotracker.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import technokek.alchotracker.R
import technokek.alchotracker.adapters.ProfileAdapter
import technokek.alchotracker.viewmodels.ProfileViewModel


class Profile : Fragment {
    private var mProfileViewModel: ProfileViewModel
    private lateinit var userText: TextView
    private lateinit var statusText: TextView
    private lateinit var friendsCounter: TextView
    private lateinit var eventsCounter: TextView
    private lateinit var avatarView: ImageView

    constructor() {
        mProfileViewModel = ProfileViewModel()
    }

    constructor(uid: String) {
        mProfileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.profile_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userText = view.findViewById(R.id.profile_user_text)
        statusText = view.findViewById(R.id.profile_status_text)
        friendsCounter = view.findViewById(R.id.profile_friends_counter)
        eventsCounter = view.findViewById(R.id.profile_events_counter)
        avatarView = view.findViewById(R.id.profile_avatar)
//        mProfileViewModel.mProfileLiveData.observe(this, {
//            userText.text = it.name
//        })
        mProfileViewModel.mProfileLiveData.observe(this, {
            userText.text = it.name
            statusText.text = it.status
            friendsCounter.text = it.friendsCount.toString()
            eventsCounter.text = it.eventCount.toString()
//            Picasso.get().load(it.avatar).into(avatarView)
        })
    }

    companion object {
        const val TAG = "ProfileFragment"
    }
}
