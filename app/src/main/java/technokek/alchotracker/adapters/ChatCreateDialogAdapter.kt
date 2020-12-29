package technokek.alchotracker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import technokek.alchotracker.R
import technokek.alchotracker.adapters.viewholders.ChatCreateDialogViewHolder
import technokek.alchotracker.api.ChatClickListener
import technokek.alchotracker.data.models.FriendModel

class ChatCreateDialogAdapter(
    private var mData: MutableList<FriendModel>,
    private val listener: ChatClickListener
) : RecyclerView.Adapter<ChatCreateDialogViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatCreateDialogViewHolder {
        return ChatCreateDialogViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.one_friend, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ChatCreateDialogViewHolder, position: Int) {
        val model = mData[position]
        Picasso.get().load(model.avatar).into(holder.mImageView)
        holder.mTextView.text = model.name
        holder.mTextView.setOnClickListener {
            listener.pressChat(model.chatID, model)
        }

        holder.relativeLayout.setOnClickListener {
            listener.pressChat(model.chatID, model)
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    fun refresh(mData: MutableList<FriendModel>) {
        this.mData = mData
    }
}
