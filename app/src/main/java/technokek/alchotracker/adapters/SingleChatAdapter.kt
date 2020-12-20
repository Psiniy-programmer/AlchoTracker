package technokek.alchotracker.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import technokek.alchotracker.R
import technokek.alchotracker.adapters.viewholders.ChatMessageViewHolder
import technokek.alchotracker.data.models.SingleChatMessageModel

class SingleChatAdapter(
    private var mData: MutableList<SingleChatMessageModel>,
    private val friendID: String
) : RecyclerView.Adapter<ChatMessageViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMessageViewHolder {
        return ChatMessageViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_chat, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ChatMessageViewHolder, position: Int) {
        val model = mData[position]

        if (friendID == model.fromId) {
            holder.mFriendLayout.visibility = View.VISIBLE
            holder.mFriendMessage.text = model.message
            holder.mFriendDate.text = model.time
            holder.mMasterLayout.visibility = View.GONE
        } else {
            holder.mMasterLayout.visibility = View.VISIBLE
            holder.mMasterMessage.text = model.message
            holder.mMasterDate.text = model.time
            holder.mFriendLayout.visibility = View.GONE
        }
    }

    fun setData(newData: MutableList<SingleChatMessageModel>) {
        mData = newData
    }

    override fun getItemCount(): Int {
        return mData.size
    }
}