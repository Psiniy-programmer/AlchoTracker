package technokek.alchotracker.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import technokek.alchotracker.R
import technokek.alchotracker.adapters.viewholders.ChatListViewHolder
import technokek.alchotracker.api.ChatListListener
import technokek.alchotracker.data.models.ChatFriendModel

class ChatListAdapter(
    private var mData: MutableList<ChatFriendModel>,
    private val listener: ChatListListener
) :
    RecyclerView.Adapter<ChatListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListViewHolder {
        return ChatListViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_list_chat, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ChatListViewHolder, position: Int) {
        val model = mData[position]

        Picasso.get().load(model.avatar).into(holder.mImageView)
        holder.mImageView.setOnClickListener {
            listener.pressChatFriend(model.chatID, model)
        }

        holder.nameTextView.text = model.name
        holder.nameTextView.setOnClickListener {
            listener.pressChatFriend(model.chatID, model)
        }

        holder.textTextView.text = model.lastMessage
        holder.textTextView.setOnClickListener {
            listener.pressChatFriend(model.chatID, model)
        }

        holder.timeTextView.text = model.lastDateTime
        holder.timeTextView.setOnClickListener {
            listener.pressChatFriend(model.chatID, model)
        }

        holder.lineLayout.setOnClickListener {
            listener.pressChatFriend(model.chatID, model)
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    fun refresh(mData: MutableList<ChatFriendModel>) {
        this.mData = mData
    }

}