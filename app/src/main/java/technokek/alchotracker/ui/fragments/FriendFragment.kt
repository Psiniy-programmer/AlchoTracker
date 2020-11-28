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

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_friend)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val listener = context as FriendClickListener

        mFriendViewModel.friends.observe(this, {
            if (recyclerView.adapter == null) {
                val adapter = FriendAdapter(mFriendViewModel.friends.value!!, listener)
                recyclerView.adapter = adapter
            }
        })
    }

    companion object {
        const val TAG = "FriendFragment"
    }
}
