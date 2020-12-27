package technokek.alchotracker.ui.fragments

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import technokek.alchotracker.R
import technokek.alchotracker.adapters.ChatCreateDialogAdapter
import technokek.alchotracker.adapters.ChatListAdapter
import technokek.alchotracker.adapters.FriendAdapter
import technokek.alchotracker.api.ChatClickListener
import technokek.alchotracker.api.ChatListListener
import technokek.alchotracker.api.FriendClickListener
import technokek.alchotracker.data.models.FriendModel
import technokek.alchotracker.viewmodels.ChatViewModel

class ChatListFragment : Fragment(), ChatClickListener {

    private lateinit var mChatViewModel: ChatViewModel
    private lateinit var chatListRecyclerView: RecyclerView
    private lateinit var chatListAdapter: ChatListAdapter

    private lateinit var chatFriendListRecyclerView: RecyclerView
    private lateinit var chatFriendListAdapter: ChatCreateDialogAdapter

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
            ChatCreateDialogAdapter(
                mChatViewModel.mediatorFriendLiveData.value!!,
                this
            )
        } else {
            ChatCreateDialogAdapter(mutableListOf(), this)
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
                mChatViewModel.refreshChatID(it)
                chatListAdapter.notifyDataSetChanged()
            }
        )

        mChatViewModel.mediatorChatLiveData.observe(
            viewLifecycleOwner,
            {
                if (chatListRecyclerView.adapter == null) {
                    mProgressBar.visibility = View.GONE
                    chatListAdapter.refresh(mChatViewModel.mediatorChatLiveData.value!!)
                    chatListRecyclerView.adapter = chatListAdapter
                    Log.d("TYTAKAYASUKA", "Адаптер установлен")
                } else {

                    chatListAdapter.notifyDataSetChanged()
                    Log.d(
                        "TYTAKAYASUKA",
                        "рефреш адаптера + ${mChatViewModel.mediatorChatLiveData.value!![0].lastMessage}"
                    )
                }
            }
        )
    }

    @SuppressLint("ResourceAsColor")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.create_private_chat, menu)

        val searchItem: MenuItem = menu.findItem(R.id.private_chat)
        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                chatListRecyclerView.visibility = View.GONE
                chatFriendListRecyclerView.visibility = View.VISIBLE
                searchItem.isVisible = false

                return false
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                chatListRecyclerView.visibility = View.VISIBLE
                chatFriendListRecyclerView.visibility = View.GONE
                return false
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

        val searchText = searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        searchText.setHintTextColor(R.color.gray)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
//                    FriendAdapter.setSearchName(newText)
                }

                return false
            }
        })

        val searchManager =
            activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
    }

    override fun pressChat(chatID: String, model: FriendModel) {
        mChatViewModel.createChat(model)

        (activity as ChatClickListener).pressChat(chatID, model)
    }
}
