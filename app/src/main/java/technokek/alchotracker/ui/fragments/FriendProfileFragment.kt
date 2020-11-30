package technokek.alchotracker.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.squareup.picasso.Picasso
import technokek.alchotracker.R
import technokek.alchotracker.viewmodels.FriendProfileViewModel
import technokek.alchotracker.viewmodels.factories.FriendProfileFactory

class FriendProfileFragment : Fragment() {
    private lateinit var uid: String
    private lateinit var mProfileViewModel: FriendProfileViewModel
    private lateinit var userText: TextView
    private lateinit var statusText: TextView
    private lateinit var friendsCounter: TextView
    private lateinit var eventsCounter: TextView
    private lateinit var avatarView: ImageView
    private lateinit var favouriteDrink: TextView
    private lateinit var preferencesBtn: Button
    private lateinit var addFriendBtn: ImageButton
    private lateinit var deleteFriendBtn: ImageButton
    private lateinit var cancelRequestBtn: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.friend_profile_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uid = arguments?.get("uid") as String

        savedInstanceState?.let {
            uid = savedInstanceState.getString("uid").toString()
        }
        userText = view.findViewById(R.id.friend_profile_user_text)
        statusText = view.findViewById(R.id.friend_profile_status_text)
        friendsCounter = view.findViewById(R.id.friend_profile_friends_counter)
        eventsCounter = view.findViewById(R.id.friend_profile_events_counter)
        avatarView = view.findViewById(R.id.friend_profile_avatar)
        favouriteDrink = view.findViewById(R.id.friend_drink)

        activity?.application?.let {
            mProfileViewModel = ViewModelProvider(
                this,
                FriendProfileFactory(it, uid)
            )[FriendProfileViewModel::class.java]
        }

        mProfileViewModel.profile.observe(viewLifecycleOwner, {
            userText.text = it.name
            statusText.text = it.status
            favouriteDrink.text = it.favouriteDrink
            friendsCounter.text = it.friendsCount.toString()
            eventsCounter.text = it.eventCount.toString()
            Picasso.get().load(it.avatar).into(avatarView)
        })
        preferencesBtn = view.findViewById(R.id.friend_preferences_list)

        preferencesBtn.setOnClickListener {
            val navController =
                activity?.let { it1 -> Navigation.findNavController(it1, R.id.content) }
            val bundle = Bundle()
            bundle.putString("uid", uid)
            navController?.navigate(
                R.id.action_friendProfileFragment_to_friendPreferencesFragment,
                arguments
            )
        }
        addFriendBtn = view.findViewById(R.id.add_friend_button)
        deleteFriendBtn = view.findViewById(R.id.delete_friend_button)
        cancelRequestBtn = view.findViewById(R.id.cancel_friend_button)

        mProfileViewModel.requests.observe(viewLifecycleOwner, {
            if (it.inFriend) {
                deleteFriendBtn.visibility = View.VISIBLE
                addFriendBtn.visibility = View.GONE
                cancelRequestBtn.visibility = View.GONE
            } else if (it.requestIsSended){
                cancelRequestBtn.visibility = View.VISIBLE
                deleteFriendBtn.visibility = View.GONE
                addFriendBtn.visibility = View.GONE
            } else {
                addFriendBtn.visibility = View.VISIBLE
                deleteFriendBtn.visibility = View.GONE
                cancelRequestBtn.visibility = View.GONE
            }
        })
        addFriendBtn.setOnClickListener {
            mProfileViewModel.addFriend()
        }

        deleteFriendBtn.setOnClickListener {
            mProfileViewModel.deleteFriend()
        }

        cancelRequestBtn.setOnClickListener {

        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("uid", uid)
    }

    companion object {
        const val TAG = "FriendProfileFragment"
    }
}