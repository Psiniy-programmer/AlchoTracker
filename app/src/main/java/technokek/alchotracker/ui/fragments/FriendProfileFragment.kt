package technokek.alchotracker.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import technokek.alchotracker.R
import technokek.alchotracker.viewmodels.FriendProfileViewModel
import technokek.alchotracker.viewmodels.factories.FriendProfileFactory

class FriendProfileFragment: Fragment() {
    private lateinit var uid: String
    private lateinit var mProfileViewModel: FriendProfileViewModel
    private lateinit var userText: TextView
    private lateinit var statusText: TextView
    private lateinit var friendsCounter: TextView
    private lateinit var eventsCounter: TextView
    private lateinit var avatarView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.profile_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        uid = arguments?.get("uid") as String

        savedInstanceState?.let {
            uid = savedInstanceState.getString("uid").toString()
        }

        userText = view.findViewById(R.id.profile_user_text)
        statusText = view.findViewById(R.id.profile_status_text)
        friendsCounter = view.findViewById(R.id.profile_friends_counter)
        eventsCounter = view.findViewById(R.id.profile_events_counter)
        avatarView = view.findViewById(R.id.profile_avatar)
        activity?.application?.let {
            mProfileViewModel = ViewModelProvider(this, FriendProfileFactory(it, uid))[FriendProfileViewModel::class.java]
        }

        mProfileViewModel.profile.observe(viewLifecycleOwner, {
            userText.text = it.name
            statusText.text = it.status
            friendsCounter.text = it.friendsCount.toString()
            eventsCounter.text = it.eventCount.toString()
            Picasso.get().load(it.avatar).into(avatarView)
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("uid", uid)
    }

    companion object {
        const val TAG = "FriendProfileFragment"
    }
}