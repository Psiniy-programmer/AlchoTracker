package technokek.alchotracker.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*
import technokek.alchotracker.R
import technokek.alchotracker.adapters.viewholders.ChatMessageViewHolder
import technokek.alchotracker.data.models.ChatMessageModel
import technokek.alchotracker.data.repositories.Constants.CHATS
import technokek.alchotracker.data.repositories.Constants.ALLMESSAGES

class ChatFragment : Fragment() {

    private lateinit var progressBar: ProgressBar
    private lateinit var chatID: String
    private lateinit var recyclerOptions: FirebaseRecyclerOptions<ChatMessageModel>
    private lateinit var recyclerAdapter: FirebaseRecyclerAdapter<ChatMessageModel, ChatMessageViewHolder>
    private lateinit var recyclerView: RecyclerView
    private var query: Query = FirebaseDatabase.getInstance().reference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        chatID = arguments?.get("chatID") as String

        savedInstanceState?.let {
            chatID = savedInstanceState.getString(chatID).toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater
            .inflate(R.layout.fragment_chat, container, false)

        progressBar = view.findViewById(R.id.chat_progress_bar)
        recyclerView = view.findViewById(R.id.recycler_chat)
        recyclerOptions = FirebaseRecyclerOptions.Builder<ChatMessageModel>()
            .setQuery(query, ChatMessageModel::class.java)
            .build()

        recyclerAdapter = object :
            FirebaseRecyclerAdapter<ChatMessageModel, ChatMessageViewHolder>(recyclerOptions) {

            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): ChatMessageViewHolder {
                val mView: View =
                    LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)

                return ChatMessageViewHolder(mView)
            }

            override fun onBindViewHolder(
                holder: ChatMessageViewHolder,
                position: Int,
                model: ChatMessageModel
            ) {
                val mRefMessage = query.ref.child(CHATS).child(ALLMESSAGES).child(chatID)
                mRefMessage.addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val message = snapshot.getValue(ChatMessageModel::class.java)
                        if (message != null) {
                            holder.mMessage.text = message.message
                            holder.mDate.text = message.time
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
//                        TODO("Not yet implemented")
                    }

                })


            }

        }
        recyclerView.adapter = recyclerAdapter
        recyclerAdapter.startListening()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onPause() {
        super.onPause()
        recyclerAdapter.stopListening()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("chatID", chatID)
    }
}