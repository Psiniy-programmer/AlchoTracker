package technokek.alchotracker.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import technokek.alchotracker.R
import technokek.alchotracker.adapters.EventAdapter
import technokek.alchotracker.api.EventClickListener
import technokek.alchotracker.viewmodels.EventViewModel

class EventFragment : Fragment() {

    private lateinit var mEventViewModel: EventViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mEventViewModel = ViewModelProvider(this)[EventViewModel::class.java]

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_event)
        val listener = context as EventClickListener
        val adapter = EventAdapter(mEventViewModel.events.value!!, listener)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        mEventViewModel.events.observe(this, {
            adapter.notifyDataSetChanged()
            Log.d("test", "success")
        })
    }

    companion object {
        const val TAG = "EventFragment"
    }
}
