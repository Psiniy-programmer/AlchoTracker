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
import technokek.alchotracker.adapters.FriendListAdapter
import technokek.alchotracker.adapters.MembersAdapter
import technokek.alchotracker.api.FriendToFriendClickListener
import technokek.alchotracker.viewmodels.FriendListViewModel
import technokek.alchotracker.viewmodels.FriendViewModel
import technokek.alchotracker.viewmodels.MembersViewModel

class MembersFragment: Fragment() {

    private lateinit var eventId: String
    private lateinit var mMembersViewModel: MembersViewModel
    private lateinit var mMembersAdapter: MembersAdapter
    private lateinit var mMembersRecyclerView: RecyclerView
    private lateinit var mProgressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater
            .inflate(R.layout.fragment_members, container, false)

        if (arguments != null) {
            val args = MembersFragmentArgs.fromBundle(requireArguments())
            eventId = args.eventId
        }

        activity?.title = resources.getString(R.string.title_members)

        mProgressBar = view.findViewById(R.id.indeterminateBarMembers)

        savedInstanceState?.let {
            eventId = savedInstanceState.getString("eventID").toString()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mMembersViewModel = ViewModelProvider(this)[MembersViewModel::class.java]
        mMembersViewModel.setEventID(eventId)

        mMembersRecyclerView = view.findViewById(R.id.recycler_members)
        mMembersRecyclerView.layoutManager = LinearLayoutManager(context)

        mMembersAdapter = if (mMembersViewModel.mediatorLiveData.value != null) {
            MembersAdapter(mMembersViewModel.mediatorLiveData.value!!, activity as FriendToFriendClickListener)
        } else {
            MembersAdapter(mutableListOf(), activity as FriendToFriendClickListener)
        }

        mMembersViewModel.mediatorLiveData.observe(
            viewLifecycleOwner,
            {
                if (mMembersRecyclerView.adapter == null) {
                    mProgressBar.visibility = View.GONE
                    mMembersViewModel.mediatorLiveData.value?.let { it ->
                        mMembersAdapter.refresh(
                            it
                        )
                    }
                    mMembersRecyclerView.adapter = mMembersAdapter
                }

                mMembersAdapter.notifyDataSetChanged()
            }
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("eventID", eventId)
    }
}
