package technokek.alchotracker.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import com.squareup.picasso.Picasso
import technokek.alchotracker.R
import technokek.alchotracker.api.FriendProfileChatClickListener
import technokek.alchotracker.api.FriendToFriendClickListener
import technokek.alchotracker.data.models.FriendModel
import technokek.alchotracker.viewmodels.FriendProfileViewModel
import technokek.alchotracker.viewmodels.factories.FriendProfileFactory

class FriendProfileFragment : Fragment() {

    private lateinit var uid: String
    private lateinit var mProfileViewModel: FriendProfileViewModel
    private lateinit var userText: MaterialTextView
    private lateinit var statusText: MaterialTextView
    private lateinit var friendsCounter: MaterialTextView
    private lateinit var eventsCounter: MaterialTextView
    private lateinit var avatarView: ImageView
    private lateinit var preferencesBtn: MaterialCardView
    private lateinit var friendsBtn: MaterialCardView
    private lateinit var chatBtn: MaterialCardView
    private lateinit var addBtn: MenuItem
    private lateinit var deleteBtn: MenuItem
    private lateinit var cancelBtn: MenuItem

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.friend_profile_fragment, container, false)
    }

    @SuppressLint("ShowToast")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uid = arguments?.get("uid") as String

        setHasOptionsMenu(true)

        savedInstanceState?.let {
            uid = savedInstanceState.getString("uid").toString()
        }
        userText = view.findViewById(R.id.friend_profile_user_text)
        statusText = view.findViewById(R.id.friend_profile_status_text)
        friendsCounter = view.findViewById(R.id.friend_profile_friends_counter)
        eventsCounter = view.findViewById(R.id.friend_profile_events_counter)
        avatarView = view.findViewById(R.id.friend_profile_avatar)
        chatBtn = view.findViewById(R.id.friend_chats_btn)
        friendsBtn = view.findViewById(R.id.friend_profile_friends_btn)
        preferencesBtn = view.findViewById(R.id.friend_preferences_list_btn)

        activity?.application?.let {
            mProfileViewModel = ViewModelProvider(
                this,
                FriendProfileFactory(it, uid)
            )[FriendProfileViewModel::class.java]
        }

        activity?.title = mProfileViewModel.profile.value?.name
        mProfileViewModel.profile.observe(
            viewLifecycleOwner,
            {
                userText.text = it.name
                statusText.text = it.status
                friendsCounter.text = it.friendsCount.toString()
                eventsCounter.text = it.eventCount.toString()
                Picasso.get().load(it.avatar).into(avatarView)
                activity?.title = it.name
            }
        )

        chatBtn.setOnClickListener {
            mProfileViewModel.profile.value?.let { it1 ->
                if (it1.chatID.isNotEmpty())
                    (activity as FriendProfileChatClickListener).pressChatFromFriend(
                        it1.chatID, it1.avatar, it1.name, it1.userID
                    )
                else
                    Toast.makeText(context, resources.getString(R.string.out_of_chat), Toast.LENGTH_LONG).show()
            }
        }

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

        friendsBtn.setOnClickListener {
            (activity as FriendToFriendClickListener).pressFriendToFriend(uid)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.friend_profile_menu, menu)

        addBtn = menu.findItem(R.id.action_add_friend_button)
        deleteBtn = menu.findItem(R.id.action_delete_friend_button)
        cancelBtn = menu.findItem(R.id.action_cancel_request_button)

        mProfileViewModel.requests.observe(viewLifecycleOwner, {
            if (it.requestIsSended) {
                cancelBtn.isVisible = true
                addBtn.isVisible = false
                deleteBtn.isVisible = false
            } else {
                if (it.inFriend) {
                    deleteBtn.isVisible = true
                    addBtn.isVisible = false
                    cancelBtn.isVisible = false
                } else {
                    addBtn.isVisible = true
                    deleteBtn.isVisible = false
                    cancelBtn.isVisible = false
                }
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add_friend_button -> mProfileViewModel.addFriend()
            R.id.action_delete_friend_button -> mProfileViewModel.deleteFriend()
            R.id.action_cancel_request_button -> mProfileViewModel.cancelRequest()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("uid", uid)
    }

    companion object {
        const val TAG = "FriendProfileFragment"
    }
}
