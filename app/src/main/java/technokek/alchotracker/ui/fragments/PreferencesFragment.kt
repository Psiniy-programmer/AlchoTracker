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
import technokek.alchotracker.adapters.PreferencesAdapter
import technokek.alchotracker.viewmodels.PreferencesViewModel

class PreferencesFragment : Fragment() {

    private lateinit var mPreferencesViewModel: PreferencesViewModel
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.preferences, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mPreferencesViewModel = ViewModelProvider(this)[PreferencesViewModel::class.java]
        recyclerView = view.findViewById(R.id.profile_preferences_list)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val adapter = if (mPreferencesViewModel.preferences.value != null) {
            PreferencesAdapter(mPreferencesViewModel.preferences.value!!)
        } else {
            PreferencesAdapter(mutableListOf())
        }

        mPreferencesViewModel.preferences.observe(viewLifecycleOwner, {
            if (recyclerView.adapter == null) {
                adapter.refresh(mPreferencesViewModel.preferences.value!!)
                adapter.notifyDataSetChanged()
                recyclerView.adapter = adapter
            }
        })
    }
}