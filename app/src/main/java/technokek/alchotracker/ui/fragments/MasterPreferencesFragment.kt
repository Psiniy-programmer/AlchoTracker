package technokek.alchotracker.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import technokek.alchotracker.R
import technokek.alchotracker.adapters.MasterPreferencesAdapter
import technokek.alchotracker.api.PreferencesClickListener
import technokek.alchotracker.viewmodels.MasterPreferencesViewModel

class MasterPreferencesFragment : Fragment(), PreferencesClickListener {

    private lateinit var mPreferencesViewModel: MasterPreferencesViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var addBtn: Button
    private lateinit var editText: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.master_preferences, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mPreferencesViewModel = ViewModelProvider(this)[MasterPreferencesViewModel::class.java]
        recyclerView = view.findViewById(R.id.profile_preferences_list)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val listener = this as PreferencesClickListener

        val adapter = if (mPreferencesViewModel.preferences.value != null) {
            MasterPreferencesAdapter(mPreferencesViewModel.preferences.value!!, listener)
        } else {
            MasterPreferencesAdapter(mutableListOf(), listener)
        }

        mPreferencesViewModel.preferences.observe(
            viewLifecycleOwner,
            {
                if (recyclerView.adapter == null) {
                    adapter.refresh(it)
                    adapter.notifyDataSetChanged()
                    recyclerView.adapter = adapter
                } else {
                    adapter.refresh(it)
                    adapter.notifyDataSetChanged()
                }
            }
        )
        addBtn = view.findViewById(R.id.add_preference_button)
        editText = view.findViewById(R.id.add_preference_edit_text)
        addBtn.setOnClickListener {
            mPreferencesViewModel.addPreferenceItem(editText.text.toString())
            editText.setText("")
        }
    }
    override fun pressEventAddPreference(text: String) {
        mPreferencesViewModel.removePreferenceItem(text)
    }
}
