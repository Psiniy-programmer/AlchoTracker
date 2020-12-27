package technokek.alchotracker.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import com.squareup.picasso.Picasso
import technokek.alchotracker.R
import technokek.alchotracker.viewmodels.MasterProfileViewModel

class MasterProfileFragment : Fragment() {
    private lateinit var userText: MaterialTextView
    private lateinit var statusText: MaterialTextView
    private lateinit var friendsCounter: MaterialTextView
    private lateinit var eventsCounter: MaterialTextView
    private lateinit var avatarView: ImageView
    private lateinit var preferencesBtn: MaterialCardView
    private lateinit var friendsBtn: MaterialCardView
    private lateinit var partiesBtn: MaterialCardView
    private lateinit var chatsBtn: MaterialCardView
    private lateinit var favouriteDrink: MaterialTextView
    private lateinit var mProfileViewModel: MasterProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = resources.getString(R.string.master_profile_toolbar)

        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.master_profile_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userText = view.findViewById(R.id.master_profile_user_text)
        statusText = view.findViewById(R.id.master_profile_status_text)
        friendsCounter = view.findViewById(R.id.master_profile_friends_counter)
        eventsCounter = view.findViewById(R.id.master_profile_events_counter)
        avatarView = view.findViewById(R.id.master_profile_avatar)
        favouriteDrink = view.findViewById(R.id.master_drink)
        preferencesBtn = view.findViewById(R.id.master_preferences_list_btn)
        friendsBtn = view.findViewById(R.id.master_profile_friends_btn)
        partiesBtn = view.findViewById(R.id.master_profile_parties_btn)
        chatsBtn = view.findViewById(R.id.master_chats_btn)
        mProfileViewModel = ViewModelProvider(this)[MasterProfileViewModel()::class.java]


        mProfileViewModel.profile.observe(
            viewLifecycleOwner,
            {
                userText.text = it.name
                statusText.text = it.status
                favouriteDrink.text = it.favouriteDrink
                friendsCounter.text = it.friendsCount.toString()
                eventsCounter.text = it.eventCount.toString()
                Picasso.get().load(it.avatar).into(avatarView)
            }
        )

        friendsBtn.setOnClickListener {
            navigateFun(R.id.action_masterProfileFragment_to_friendFragment)
        }

        partiesBtn.setOnClickListener {
            navigateFun(R.id.action_masterProfileFragment_to_eventFragment)
        }

        chatsBtn.setOnClickListener {
            navigateFun(R.id.action_masterProfileFragment_to_chatListFragment)
        }

        preferencesBtn.setOnClickListener {
            navigateFun(R.id.action_masterProfileFragment_to_preferencesFragment)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.master_profile_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_settings) {
            val navController =
                activity?.let { it1 -> Navigation.findNavController(it1, R.id.content) }
            navController?.navigate(R.id.action_masterProfileFragment_to_profileSettingsFragment)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun navigateFun(resId: Int) {
        val navController =
            activity?.let { it1 -> Navigation.findNavController(it1, R.id.content) }
        navController?.navigate(resId)
    }

    companion object {
        const val TAG = "MasterProfileFragment"
    }
}
