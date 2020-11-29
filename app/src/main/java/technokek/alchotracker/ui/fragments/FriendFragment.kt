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
import technokek.alchotracker.adapters.FriendRequestAdapter
import technokek.alchotracker.api.FriendClickListener
import technokek.alchotracker.api.RequestClickListener
import technokek.alchotracker.viewmodels.FriendViewModel

class FriendFragment : Fragment(), RequestClickListener {

    private lateinit var mFriendViewModel: FriendViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_friend, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mFriendViewModel = ViewModelProvider(this)[FriendViewModel::class.java]
        val mProgressBar = view.findViewById<ProgressBar>(R.id.indeterminateBarFriend)

        val requestRecyclerView = view.findViewById<RecyclerView>(R.id.recycler_friend_request)
        requestRecyclerView.layoutManager = LinearLayoutManager(context)
        val friendRecyclerView = view.findViewById<RecyclerView>(R.id.recycler_friend)
        friendRecyclerView.layoutManager = LinearLayoutManager(context)
        val listener = context as FriendClickListener

        val adapterRequest = if (mFriendViewModel.mediatorRequestLiveData.value != null) {
            FriendRequestAdapter(mFriendViewModel.mediatorRequestLiveData.value!!, listener, this as RequestClickListener)
        } else {
            FriendRequestAdapter(mutableListOf(), listener, this as RequestClickListener)
        }
        val adapterFriend = if (mFriendViewModel.mediatorFriendLiveData.value != null) {
            FriendAdapter(mFriendViewModel.mediatorFriendLiveData.value!!, listener)
        } else {
            FriendAdapter(mutableListOf(), listener)
        }

        mFriendViewModel.mediatorFriendLiveData.observe(this, {
            if (friendRecyclerView.adapter == null) {
                mProgressBar.visibility = View.GONE
                adapterFriend.refresh(mFriendViewModel.mediatorFriendLiveData.value!!)
                adapterFriend.notifyDataSetChanged()
                friendRecyclerView.adapter = adapterFriend
            }
        })

        mFriendViewModel.mediatorRequestLiveData.observe(this, {
            if (requestRecyclerView.adapter == null) {
                mProgressBar.visibility = View.GONE
                adapterRequest.refresh(mFriendViewModel.mediatorRequestLiveData.value!!)
                adapterFriend.notifyDataSetChanged()
                requestRecyclerView.adapter = adapterRequest
            }
        })
    }

    override fun accept(uid: String) {
        mFriendViewModel.acceptRequest(uid)
    }

    override fun deny(uid: String) {
        mFriendViewModel.denyRequest(uid)
    }

    companion object {
        const val TAG = "FriendFragment"
    }
}
