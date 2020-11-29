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
import technokek.alchotracker.api.FriendClickListener
import technokek.alchotracker.viewmodels.FriendViewModel

class FriendFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_friend, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mFriendViewModel = ViewModelProvider(this)[FriendViewModel::class.java]
        val mProgressBar = view.findViewById<ProgressBar>(R.id.indeterminateBarFriend)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_friend)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val listener = context as FriendClickListener

        var adapter = if (mFriendViewModel.friends.value != null) {
            FriendAdapter(mFriendViewModel.friends.value!!, listener)
        } else {
            FriendAdapter(mutableListOf(), listener)
        }

        mFriendViewModel.friends.observe(this, {
            if (recyclerView.adapter == null) {
                mProgressBar.visibility = View.GONE
                adapter.refresh(mFriendViewModel.friends.value!!)
                adapter.notifyDataSetChanged()
                recyclerView.adapter = adapter
            }
        })
    }

    companion object {
        const val TAG = "FriendFragment"
    }
}
