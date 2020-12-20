package technokek.alchotracker.ui.fragments

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ProgressBar
import android.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import technokek.alchotracker.R
import technokek.alchotracker.adapters.ChatListAdapter
import technokek.alchotracker.adapters.FriendAdapter
import technokek.alchotracker.api.ChatListListener
import technokek.alchotracker.api.FriendClickListener
import technokek.alchotracker.viewmodels.ChatViewModel

class ChatListFragment : Fragment() {

    private lateinit var mChatViewModel: ChatViewModel
    private lateinit var chatListRecyclerView: RecyclerView
    private lateinit var chatListAdapter: ChatListAdapter

    private lateinit var chatFriendListRecyclerView: RecyclerView
    private lateinit var chatFriendListAdapter: FriendAdapter

    private lateinit var mProgressBar: ProgressBar
    private lateinit var buttonView: Button

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

        chatFriendListRecyclerView = view.findViewById(R.id.chat_list_friend)
        chatFriendListRecyclerView.layoutManager = LinearLayoutManager(context)

        chatListAdapter = if (mChatViewModel.mediatorChatLiveData.value != null) {
            ChatListAdapter(
                mChatViewModel.mediatorChatLiveData.value!!,
                activity as ChatListListener
            )
        } else {
            ChatListAdapter(mutableListOf(), activity as ChatListListener)
        }

        chatFriendListAdapter = if (mChatViewModel.mediatorFriendLiveData.value != null) {
            FriendAdapter(
                mChatViewModel.mediatorFriendLiveData.value!!,
                activity as FriendClickListener
            )
        } else {
            FriendAdapter(mutableListOf(), activity as FriendClickListener)
        }

        mChatViewModel.mediatorFriendLiveData.observe(
            viewLifecycleOwner,
            {
                if (chatFriendListRecyclerView.adapter == null) {
                    mProgressBar.visibility = View.GONE
                    chatFriendListAdapter.refresh(mChatViewModel.mediatorFriendLiveData.value!!)
                    chatFriendListRecyclerView.adapter = chatFriendListAdapter
                }

                chatFriendListAdapter.notifyDataSetChanged()
            }
        )

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
        inflater.inflate(R.menu.create_private_chat, menu)

        val searchItem: MenuItem = menu.findItem(R.id.private_chat)
        searchItem.isVisible = true
        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                chatListRecyclerView.visibility = View.GONE
                chatFriendListRecyclerView.visibility = View.VISIBLE
                searchItem.isVisible = false

                return false
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                return false
            }
        })

        buttonView = searchItem.actionView as Button
//        buttonView.visibility = View.GONE
//        buttonView.setOnCloseListener { true }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            chatListRecyclerView.visibility = View.VISIBLE
            chatFriendListRecyclerView.visibility = View.GONE
            item.isVisible = true
        }

        return super.onOptionsItemSelected(item)
    }
}
