package technokek.alchotracker.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import technokek.alchotracker.R
import technokek.alchotracker.adapters.SingleChatAdapter
import technokek.alchotracker.ui.activity.MainActivity
import technokek.alchotracker.viewmodels.SingleChatViewModel
import technokek.alchotracker.viewmodels.factories.SingleChatViewModelFactory

class SignleChatFragment : Fragment() {

    private lateinit var progressBar: ProgressBar
    private lateinit var chatID: String
    private lateinit var friendID: String
    private lateinit var name: String
    private lateinit var avatar: String
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: SingleChatAdapter
    private lateinit var mChatViewModel: SingleChatViewModel
    private lateinit var mButton: Button
    private lateinit var mEditText: EditText
    private lateinit var mChatAvatar: ImageView
    private lateinit var mChatTittle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        chatID = arguments?.get("chatID") as String
        friendID = arguments?.get("uid") as String
        name = arguments?.get("name") as String
        avatar = arguments?.get("avatar") as String

        savedInstanceState?.let {
            chatID = savedInstanceState.getString(chatID).toString()
        }
    }

    override fun onResume() {
        (activity as MainActivity).supportActionBar?.hide()
        super.onResume()
    }

    override fun onStop() {
        (activity as MainActivity).supportActionBar?.show()
        super.onStop()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater
            .inflate(R.layout.fragment_chat, container, false)

        val linearLayoutManager = LinearLayoutManager(context)
        progressBar = view.findViewById(R.id.chat_progress_bar)
        mRecyclerView = view.findViewById(R.id.recycler_chat)
        mButton = view.findViewById(R.id.button_chat)
        mEditText = view.findViewById(R.id.edit_text_chat)
        mChatAvatar = view.findViewById(R.id.chat_image)
        mChatTittle = view.findViewById(R.id.chat_name)
        mRecyclerView.layoutManager = linearLayoutManager
        mRecyclerView.setHasFixedSize(true)

        Picasso.get()
            .load(avatar)
            .fit()
            .centerCrop()
            .into(mChatAvatar)
//        mChatTittle.text = name

        activity?.application?.let {
            mChatViewModel = ViewModelProvider(
                this,
                SingleChatViewModelFactory(it, chatID)
            )[SingleChatViewModel::class.java]
        }

        mAdapter = if (mChatViewModel.chat.value != null)
            SingleChatAdapter(mChatViewModel.chat.value!!, friendID)
        else
            SingleChatAdapter(mutableListOf(), friendID)

        mChatViewModel.chat.observe(viewLifecycleOwner, {
            if (mRecyclerView.adapter == null) {
                mAdapter.setData(it)
                mAdapter.notifyDataSetChanged()
                mRecyclerView.adapter = mAdapter
                progressBar.visibility = View.GONE
            } else {
                mAdapter.setData(it)
                mAdapter.notifyDataSetChanged()
            }
        })

        mButton.setOnClickListener {
            mChatViewModel.sendMessage(mEditText.text.toString())
            mEditText.setText("")
        }

        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("chatID", chatID)
    }
}