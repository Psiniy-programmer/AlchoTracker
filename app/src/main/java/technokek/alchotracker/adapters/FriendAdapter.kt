package technokek.alchotracker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import technokek.alchotracker.R
import technokek.alchotracker.adapters.viewholders.FriendViewHolder
import technokek.alchotracker.api.FriendClickListener
import technokek.alchotracker.data.Constants.*
import technokek.alchotracker.data.models.FriendModel

class FriendAdapter(
    private var mData: MutableList<FriendModel>,
    private val listener: FriendClickListener
) :
    RecyclerView.Adapter<FriendViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        return FriendViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.one_friend, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        val model = mData[position]
        Picasso.get().load(model.avatar).into(holder.mImageView)
        holder.mTextView.text = model.name
        holder.mTextView.setOnClickListener {
            if (model.chatID.isNullOrEmpty()) {
                listener.pressFriend(model.id)
            } else {
//                CoroutineScope(Dispatchers.IO).launch {
                    val ref = FirebaseDatabase
                        .getInstance()
                        .getReference("users")
                    val refChats = FirebaseDatabase
                        .getInstance()
                        .getReference("chats")
                    val currentUser = FirebaseAuth.getInstance().currentUser

                if (model.bool) {
                    ref.child(currentUser!!.uid).child(CHATID).child(model.chatID).apply {
                        child(USERS).setValue(model.chatID)
                    }
                    ref.child(model.id).child(CHATID).child(model.chatID).apply {
                        child(USERS).setValue(model.chatID)
                    }
                    refChats.child(model.chatID).apply {
                        child(ALLMESSAGES).setValue("")
                        child(LASTMESSAGE).setValue("")
                        child(LASTSENDERID).setValue("")
                        child(LASTDATETIME).setValue("")
                        child(USERS).setValue(model.chatID)
                    }
                }
                    listener.pressChat(model.id)
//                }
            }
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    fun refresh(mData: MutableList<FriendModel>) {
        this.mData = mData
    }
}
