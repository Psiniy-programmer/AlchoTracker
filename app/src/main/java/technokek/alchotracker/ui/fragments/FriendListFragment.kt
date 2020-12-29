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
import technokek.alchotracker.adapters.FriendAdapter
import technokek.alchotracker.adapters.FriendListAdapter
import technokek.alchotracker.api.FriendClickListener
import technokek.alchotracker.api.FriendToFriendClickListener
import technokek.alchotracker.viewmodels.FriendListViewModel
import technokek.alchotracker.viewmodels.FriendViewModel

class FriendListFragment : Fragment() {

    private lateinit var uid: String
    private lateinit var friendListViewModel: FriendListViewModel
    private lateinit var friendListRecyclerView: RecyclerView
    private lateinit var friendListAdapter: FriendListAdapter
    private lateinit var mProgressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater
            .inflate(R.layout.fragment_friend_list, container, false)

        activity?.title = resources.getString(R.string.friend_title)
        uid = arguments?.get("uid") as String
        mProgressBar = view.findViewById(R.id.indeterminateBarFriendToFriend)

        savedInstanceState?.let {
            uid = savedInstanceState.getString("uid").toString()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        friendListRecyclerView = view.findViewById(R.id.recycler_friend_to_friend)
        friendListRecyclerView.layoutManager = LinearLayoutManager(context)

        friendListViewModel = ViewModelProvider(this)[FriendListViewModel::class.java]
        friendListViewModel.setFriendID(uid)

        friendListAdapter = if (friendListViewModel.mediatorFriendLiveData.value != null) {
            FriendListAdapter(friendListViewModel.mediatorFriendLiveData.value!!, activity as FriendToFriendClickListener)
        } else {
            FriendListAdapter(mutableListOf(), activity as FriendToFriendClickListener)
        }

        friendListViewModel.mediatorFriendLiveData.observe(
            viewLifecycleOwner,
            {
                if (friendListRecyclerView.adapter == null) {
                    mProgressBar.visibility = View.GONE
                    friendListViewModel.mediatorFriendLiveData.value?.let { it ->
                        friendListAdapter.refresh(
                            it
                        )
                    }
                    friendListRecyclerView.adapter = friendListAdapter
                }

                friendListAdapter.notifyDataSetChanged()
            }
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("uid", uid)
    }
}
