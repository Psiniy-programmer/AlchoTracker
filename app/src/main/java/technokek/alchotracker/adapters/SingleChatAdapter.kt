package technokek.alchotracker.adapters

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

        if (friendID == model.id) {
            holder.mFriendLayout.visibility = View.VISIBLE
            holder.mFriendMessage.text = model.text_message
            holder.mFriendDate.text = model.date_time
            holder.mMasterLayout.visibility = View.GONE
        } else {
            holder.mMasterLayout.visibility = View.VISIBLE
            holder.mMasterMessage.text = model.text_message
            holder.mMasterDate.text = model.date_time
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