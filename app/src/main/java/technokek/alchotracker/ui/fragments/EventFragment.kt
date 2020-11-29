package technokek.alchotracker.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
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
        val mProgressBar = view.findViewById<ProgressBar>(R.id.indeterminateBarEvent)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_event)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val listener = context as EventClickListener

        val adapter = if (mEventViewModel.events.value != null) {
            EventAdapter(mEventViewModel.events.value!!, listener)
        } else {
            EventAdapter(mutableListOf(), listener)
        }

        mEventViewModel.events.observe(this, {
            if (recyclerView.adapter == null) {
                mProgressBar.visibility = View.GONE
                adapter.refresh(mEventViewModel.events.value!!)
                adapter.notifyDataSetChanged()
                recyclerView.adapter = adapter
            }
        })
    }

    companion object {
        const val TAG = "EventFragment"
    }
}
