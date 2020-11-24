package technokek.alchotracker.ui.fragments

import technokek.alchotracker.adapters.ProfileAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import technokek.alchotracker.R
import technokek.alchotracker.viewmodels.ProfileViewModel
// TODO Добавить прокидывание ID
class Profile : Fragment() {

    private var mProfileViewModel = ProfileViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.profile_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val avatarText = view.findViewById<TextView>(R.id.profile_avatar_text)
        val statusText = view.findViewById<TextView>(R.id.profile_status_text)
        val friendsCounter = view.findViewById<TextView>(R.id.profile_friends_counter)
        val datesCounter = view.findViewById<TextView>(R.id.profile_dates_counter)

        mProfileViewModel.mProfileLiveData.observe(this, {
            avatarText.text = it.name
            statusText.text = it.status
            friendsCounter.text = it.friendsCount.toString()
            datesCounter.text = it.datesCount.toString()
            val recyclerView: RecyclerView =
                view.findViewById(R.id.profile_preferences_list)
            recyclerView.layoutManager = LinearLayoutManager(context)
            val adapter = it?.let { ProfileAdapter(it.preferencesList) }
            recyclerView.layoutManager
            recyclerView.adapter = adapter
        })
    }

    companion object {
        const val TAG = "ProfileFragment"
    }
}
