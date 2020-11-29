package technokek.alchotracker.ui.fragments

import android.os.Bundle
import android.util.Log
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
    private lateinit var requestRecyclerView: RecyclerView
    private lateinit var friendRecyclerView: RecyclerView
    private lateinit var adapterRequest: FriendRequestAdapter
    private lateinit var adapterFriend: FriendAdapter

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

        mFriendViewModel.mediatorFriendLiveData.observe(viewLifecycleOwner, {
            if (friendRecyclerView.adapter == null) {
                mProgressBar.visibility = View.GONE
                adapterFriend.refresh(mFriendViewModel.mediatorFriendLiveData.value!!)
                adapterFriend.notifyDataSetChanged()
                friendRecyclerView.adapter = adapterFriend
            }
            adapterFriend.notifyDataSetChanged()
        })

        mFriendViewModel.mediatorRequestLiveData.observe(viewLifecycleOwner, {
            if (requestRecyclerView.adapter == null) {
                mProgressBar.visibility = View.GONE
                adapterRequest.refresh(mFriendViewModel.mediatorRequestLiveData.value!!)
                adapterRequest.notifyDataSetChanged()
                requestRecyclerView.adapter = adapterRequest
            }

            adapterRequest.notifyDataSetChanged()
        })

        mFriendViewModel.mediatorCurrentUser.observe(viewLifecycleOwner, {
            if (mFriendViewModel.mediatorFriendLiveData.value != null) {
                adapterFriend.refresh(mFriendViewModel.mediatorFriendLiveData.value!!)
                adapterFriend.notifyDataSetChanged()
            }

            if (mFriendViewModel.mediatorRequestLiveData.value != null) {
                adapterRequest.refresh(mFriendViewModel.mediatorRequestLiveData.value!!)
                adapterRequest.notifyDataSetChanged()
            }
            Log.d("Currentuser", "СРАБАТЫВАЕТ")
        })
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
