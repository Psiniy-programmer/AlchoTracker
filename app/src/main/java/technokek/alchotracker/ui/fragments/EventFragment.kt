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
import technokek.alchotracker.adapters.EventAdapter
import technokek.alchotracker.api.EventClickListener
import technokek.alchotracker.viewmodels.EventViewModel

class EventFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mEventViewModel = ViewModelProvider(this)[EventViewModel::class.java]

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_event)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val listener = context as EventClickListener

        mEventViewModel.events.observe(this, {
            if (recyclerView.adapter == null) {
                val adapter = EventAdapter(mEventViewModel.events.value!!, listener)
                recyclerView.adapter = adapter
            }
        })
    }

    companion object {
        const val TAG = "EventFragment"
    }
}
