package technokek.alchotracker.ui.fragments

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import technokek.alchotracker.R
import technokek.alchotracker.adapters.ChatListAdapter
import technokek.alchotracker.api.ChatListListener
import technokek.alchotracker.viewmodels.ChatViewModel

class ChatListFragment : Fragment() {

    private lateinit var mChatViewModel: ChatViewModel
    private lateinit var chatListRecyclerView: RecyclerView
    private lateinit var chatListAdapter: ChatListAdapter

    private lateinit var mProgressBar: ProgressBar
    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater
            .inflate(R.layout.fragment_list_chat, container, false)

        mProgressBar = view.findViewById(R.id.chat_list_progress_bar)
        activity?.title = "Chat"

        setHasOptionsMenu(true)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mChatViewModel = ViewModelProvider(this)[ChatViewModel::class.java]

        chatListRecyclerView = view.findViewById(R.id.chat_list_view)
        chatListRecyclerView.layoutManager = LinearLayoutManager(context)

        chatListAdapter = if (mChatViewModel.mediatorChatLiveData.value != null) {
            ChatListAdapter(mChatViewModel.mediatorChatLiveData.value!!, activity as ChatListListener)
        } else {
            ChatListAdapter(mutableListOf(), activity as ChatListListener)
        }

        mChatViewModel.mediatorChatListLiveData.observe(
            viewLifecycleOwner,
            {
                mChatViewModel.refresh(it)
            }
        )

        mChatViewModel.mediatorChatLiveData.observe(
            viewLifecycleOwner,
            {
                if (chatListRecyclerView.adapter == null) {
                    mProgressBar.visibility = View.GONE
                    chatListAdapter.refresh(mChatViewModel.mediatorChatLiveData.value!!)
                    chatListRecyclerView.adapter = chatListAdapter
                }

                chatListAdapter.notifyDataSetChanged()
            }
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_friends, menu)

        val searchItem: MenuItem = menu.findItem(R.id.search_friend)
        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                TODO("Not yet implemented")
            }
        })

        searchView = searchItem.actionView as SearchView
        searchView.setOnCloseListener { true }

        val searchPlateView = searchView.findViewById<View>(androidx.appcompat.R.id.search_plate)
        searchPlateView.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                android.R.color.transparent
            )
        )

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                TODO("Not yet implemented")
            }
        })

        val searchManager =
            activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
    }
}
