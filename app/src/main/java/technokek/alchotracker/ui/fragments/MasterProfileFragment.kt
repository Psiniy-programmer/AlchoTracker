package technokek.alchotracker.ui.fragments

import android.os.Bundle
import android.util.Log
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
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import technokek.alchotracker.R
import technokek.alchotracker.viewmodels.MasterProfileViewModel

class MasterProfileFragment : Fragment() {
    private lateinit var userText: TextView
    private lateinit var statusText: TextView
    private lateinit var friendsCounter: TextView
    private lateinit var eventsCounter: TextView
    private lateinit var avatarView: ImageView
    private lateinit var settingsBtn: ImageButton
    private lateinit var preferencesBtn: Button
    private lateinit var favouriteDrink: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        val mProfileViewModel = ViewModelProvider(this)[MasterProfileViewModel()::class.java]

        mProfileViewModel.profile.observe(viewLifecycleOwner, {
            userText.text = it.name
            statusText.text = it.status
            favouriteDrink.text = it.favouriteDrink
            friendsCounter.text = it.friendsCount.toString()
            eventsCounter.text = it.eventCount.toString()
            Picasso.get().load(it.avatar).into(avatarView)
        })
        settingsBtn = view.findViewById(R.id.settings_button)
        preferencesBtn = view.findViewById(R.id.master_preferences_list_btn)

        settingsBtn.setOnClickListener {
            val navController =
                activity?.let { it1 -> Navigation.findNavController(it1, R.id.content) }
            navController?.navigate(R.id.action_masterProfileFragment_to_profileSettingsFragment)
        }

        preferencesBtn.setOnClickListener {
            val navController =
                activity?.let { it1 -> Navigation.findNavController(it1, R.id.content) }
            navController?.navigate(R.id.action_masterProfileFragment_to_preferencesFragment)
        }
    }

    companion object {
        const val TAG = "MasterProfileFragment"
    }
}

