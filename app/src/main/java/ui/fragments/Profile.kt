package ui.fragments

import adapters.ProfileAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import technokek.alchotracker.R
import viewmodels.ProfileViewModel

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
        val recyclerView: RecyclerView =
            view.findViewById(R.id.profile_preferences_list)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter =
            mProfileViewModel.mProfileLiveData.value?.let { ProfileAdapter(it.preferencesList) }
        recyclerView.layoutManager
        recyclerView.adapter = adapter
    }

    companion object {
        const val TAG = "ProfileFragment"
    }
}