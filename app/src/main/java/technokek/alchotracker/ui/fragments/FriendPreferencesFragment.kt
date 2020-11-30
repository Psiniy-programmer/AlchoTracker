package technokek.alchotracker.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import technokek.alchotracker.R
import technokek.alchotracker.adapters.FriendPreferencesAdapter
import technokek.alchotracker.viewmodels.FriendPreferencesViewModel
import technokek.alchotracker.viewmodels.factories.FriendPreferencesFactory

class FriendPreferencesFragment : Fragment() {
    private lateinit var uid: String

    private lateinit var mPreferencesViewModel: FriendPreferencesViewModel
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.friend_preferences, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        uid = arguments?.get("uid") as String

        savedInstanceState?.let {
            uid = savedInstanceState.getString("uid").toString()
        }

        activity?.application?.let {
            mPreferencesViewModel = ViewModelProvider(
                this,
                FriendPreferencesFactory(it, uid)
            )[FriendPreferencesViewModel::class.java]
        }

        recyclerView = view.findViewById(R.id.friend_preferences_list)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val adapter = if (mPreferencesViewModel.preferences.value != null) {
            FriendPreferencesAdapter(mPreferencesViewModel.preferences.value!!)
        } else {
            FriendPreferencesAdapter(mutableListOf())
        }

        mPreferencesViewModel.preferences.observe(viewLifecycleOwner) {
            if (recyclerView.adapter == null) {
                adapter.refresh(mPreferencesViewModel.preferences.value!!)
                adapter.notifyDataSetChanged()
                recyclerView.adapter = adapter
            } else {
                adapter.refresh(mPreferencesViewModel.preferences.value!!)
                adapter.notifyDataSetChanged()
            }
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("uid", uid)
    }

    companion object {
        const val TAG = "FriendPreferencesFragment"
    }
}