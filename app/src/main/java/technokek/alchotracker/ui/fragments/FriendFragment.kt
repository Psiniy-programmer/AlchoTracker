package technokek.alchotracker.ui.fragments

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import technokek.alchotracker.R
import technokek.alchotracker.adapters.FriendAdapter
import technokek.alchotracker.adapters.FriendRequestAdapter
import technokek.alchotracker.adapters.SearchFriendAdapter
import technokek.alchotracker.api.FoundUserListener
import technokek.alchotracker.api.FriendClickListener
import technokek.alchotracker.api.RequestClickListener
import technokek.alchotracker.viewmodels.FriendViewModel

class FriendFragment : Fragment(), RequestClickListener {

    private lateinit var mFriendViewModel: FriendViewModel
    private lateinit var requestRecyclerView: RecyclerView
    private lateinit var friendRecyclerView: RecyclerView
    private lateinit var searchFriendRecyclerView: RecyclerView
    private lateinit var adapterRequest: FriendRequestAdapter
    private lateinit var adapterFriend: FriendAdapter
    private lateinit var adapterSearchFriend: SearchFriendAdapter

    private lateinit var requestTextView: TextView
    private lateinit var friendTextView: TextView
    private lateinit var mProgressBar: ProgressBar
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        activity?.onBackPressedDispatcher?.addCallback(this, object: OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                if (!searchView.isIconified) {
//                    searchView.onActionViewCollapsed()
//                }
//            }
//        })
    }

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
        searchFriendRecyclerView = view.findViewById(R.id.search_recycler)
        searchFriendRecyclerView.layoutManager = LinearLayoutManager(context)

        val listenerFriend = context as FriendClickListener
        val listenerSearch = context as FoundUserListener

        adapterRequest = if (mFriendViewModel.mediatorRequestLiveData.value != null) {
            FriendRequestAdapter(
                mFriendViewModel.mediatorRequestLiveData.value!!,
                listenerFriend,
                this as RequestClickListener
            )
        } else {
            FriendRequestAdapter(mutableListOf(), listenerFriend, this as RequestClickListener)
        }
        adapterFriend = if (mFriendViewModel.mediatorFriendLiveData.value != null) {
            FriendAdapter(mFriendViewModel.mediatorFriendLiveData.value!!, listenerFriend)
        } else {
            FriendAdapter(mutableListOf(), listenerFriend)
        }
        adapterSearchFriend = if (mFriendViewModel.mediatorSearchLiveData.value != null) {
            SearchFriendAdapter(mFriendViewModel.mediatorSearchLiveData.value!!, listenerSearch)
        } else {
            SearchFriendAdapter(mutableListOf(), listenerSearch)
        }

        mFriendViewModel.mediatorFriendLiveData.observe(
            viewLifecycleOwner,
            {
                if (friendRecyclerView.adapter == null) {
                    mProgressBar.visibility = View.GONE
                    adapterFriend.refresh(mFriendViewModel.mediatorFriendLiveData.value!!)
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
        inflater.inflate(R.menu.search_friends, menu)

        val searchItem: MenuItem = menu.findItem(R.id.search_friend)
        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                requestRecyclerView.visibility = View.GONE
                requestTextView.visibility = View.GONE
                friendRecyclerView.visibility = View.GONE
                friendTextView.visibility = View.GONE
                searchFriendRecyclerView.visibility = View.VISIBLE

                mFriendViewModel.mediatorSearchLiveData.observe(
                    viewLifecycleOwner,
                    {
                        if (searchFriendRecyclerView.adapter == null) {
                            adapterSearchFriend.refresh(mFriendViewModel.mediatorSearchLiveData.value!!)
                            adapterSearchFriend.notifyDataSetChanged()
                            searchFriendRecyclerView.adapter = adapterSearchFriend
                        }

                        if (mFriendViewModel.mediatorSearchLiveData.value != null) {
                            adapterSearchFriend.refresh(mFriendViewModel.mediatorSearchLiveData.value!!)
                        }

                        adapterSearchFriend.notifyDataSetChanged()
                    }
                )

                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                if (mFriendViewModel.mediatorFriendLiveData.value!!.isEmpty()) {
                    friendTextView.visibility = View.GONE
                    friendRecyclerView.visibility = View.GONE
                } else {
                    friendTextView.visibility = View.VISIBLE
                    friendRecyclerView.visibility = View.VISIBLE
                }

                if (mFriendViewModel.mediatorRequestLiveData.value!!.isEmpty()) {
                    requestTextView.visibility = View.GONE
                    requestRecyclerView.visibility = View.GONE
                } else {
                    requestTextView.visibility = View.VISIBLE
                    requestRecyclerView.visibility = View.VISIBLE
                }

                searchFriendRecyclerView.visibility = View.GONE

                return true
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
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    mFriendViewModel.setSearchName(newText)
                }

                return false
            }
        })

        val searchManager =
            activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
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
