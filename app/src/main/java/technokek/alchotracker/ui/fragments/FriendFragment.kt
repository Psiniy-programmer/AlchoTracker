package technokek.alchotracker.ui.fragments

import android.app.ActionBar
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import technokek.alchotracker.R
import technokek.alchotracker.adapters.FriendAdapter
import technokek.alchotracker.adapters.FriendRequestAdapter
import technokek.alchotracker.api.FriendClickListener
import technokek.alchotracker.api.RequestClickListener
import technokek.alchotracker.viewmodels.FriendViewModel

class FriendFragment : Fragment(), RequestClickListener {

    private lateinit var mFriendViewModel: FriendViewModel
    private lateinit var requestRecyclerView: RecyclerView
    private lateinit var friendRecyclerView: RecyclerView
    private lateinit var adapterRequest: FriendRequestAdapter
    private lateinit var adapterFriend: FriendAdapter

    private lateinit var requestTextView: TextView
    private lateinit var friendTextView: TextView
    private lateinit var mProgressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater
            .inflate(R.layout.fragment_friend, container, false)

        requestTextView = view.findViewById(R.id.request_in_friends)
        friendTextView = view.findViewById(R.id.list_friends)
        mProgressBar = view.findViewById(R.id.indeterminateBarFriend)
        activity?.title = "Friends"

        setHasOptionsMenu(true)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mFriendViewModel = ViewModelProvider(this)[FriendViewModel::class.java]

        requestRecyclerView = view.findViewById(R.id.recycler_friend_request)
        requestRecyclerView.layoutManager = LinearLayoutManager(context)
        friendRecyclerView = view.findViewById(R.id.recycler_friend)
        friendRecyclerView.layoutManager = LinearLayoutManager(context)
        val listener = context as FriendClickListener

        adapterRequest = if (mFriendViewModel.mediatorRequestLiveData.value != null) {
            FriendRequestAdapter(
                mFriendViewModel.mediatorRequestLiveData.value!!,
                listener,
                this as RequestClickListener
            )
        } else {
            FriendRequestAdapter(mutableListOf(), listener, this as RequestClickListener)
        }
        adapterFriend = if (mFriendViewModel.mediatorFriendLiveData.value != null) {
            FriendAdapter(mFriendViewModel.mediatorFriendLiveData.value!!, listener)
        } else {
            FriendAdapter(mutableListOf(), listener)
        }

        mFriendViewModel.mediatorFriendLiveData.observe(
            viewLifecycleOwner,
            {
                if (friendRecyclerView.adapter == null) {
                    mProgressBar.visibility = View.GONE
                    adapterFriend.refresh(mFriendViewModel.mediatorFriendLiveData.value!!)
                    adapterFriend.notifyDataSetChanged()
                    friendRecyclerView.adapter = adapterFriend
                }
                adapterFriend.notifyDataSetChanged()

                if (mFriendViewModel.mediatorFriendLiveData.value!!.isEmpty()) {
                    friendTextView.visibility = View.GONE
                } else {
                    friendTextView.visibility = View.VISIBLE
                }
            }
        )

        mFriendViewModel.mediatorRequestLiveData.observe(
            viewLifecycleOwner,
            {
                if (requestRecyclerView.adapter == null) {
                    mProgressBar.visibility = View.GONE
                    adapterRequest.refresh(mFriendViewModel.mediatorRequestLiveData.value!!)
                    adapterRequest.notifyDataSetChanged()
                    requestRecyclerView.adapter = adapterRequest
                }

                adapterRequest.notifyDataSetChanged()
                if (mFriendViewModel.mediatorRequestLiveData.value!!.isEmpty()) {
                    requestTextView.visibility = View.GONE
                } else {
                    requestTextView.visibility = View.VISIBLE
                }
            }
        )

        mFriendViewModel.mediatorCurrentUser.observe(
            viewLifecycleOwner,
            {
                if (mFriendViewModel.mediatorFriendLiveData.value != null) {
                    adapterFriend.refresh(mFriendViewModel.mediatorFriendLiveData.value!!)
                    adapterFriend.notifyDataSetChanged()

                    if (mFriendViewModel.mediatorFriendLiveData.value!!.isEmpty()) {
                        friendTextView.visibility = View.GONE
                    } else {
                        friendTextView.visibility = View.VISIBLE
                    }
                }

                if (mFriendViewModel.mediatorRequestLiveData.value != null) {
                    adapterRequest.refresh(mFriendViewModel.mediatorRequestLiveData.value!!)
                    adapterRequest.notifyDataSetChanged()

                    if (mFriendViewModel.mediatorRequestLiveData.value!!.isEmpty()) {
                        requestTextView.visibility = View.GONE
                    } else {
                        requestTextView.visibility = View.VISIBLE
                    }
                }
                Log.d("Currentuser", "СРАБАТЫВАЕТ")
            }
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.user_info_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_search) {

        }

        return super.onOptionsItemSelected(item)
    }

    override fun accept(uid: String, pos: Int) {
        mFriendViewModel.acceptRequest(uid, pos)
        adapterRequest.notifyItemRemoved(pos)
    }

    override fun deny(uid: String, pos: Int) {
        mFriendViewModel.denyRequest(uid, pos)
        adapterRequest.notifyItemRemoved(pos)
    }

    companion object {
        const val TAG = "FriendFragment"
    }
}
