package technokek.alchotracker.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import technokek.alchotracker.R
import technokek.alchotracker.adapters.viewholders.FriendRequestViewHolder
import technokek.alchotracker.adapters.viewholders.FriendViewHolder
import technokek.alchotracker.api.FriendClickListener
import technokek.alchotracker.api.RequestClickListener
import technokek.alchotracker.data.models.FriendModel

class FriendRequestAdapter(
    private var mData: MutableList<FriendModel>,
    private val listener: FriendClickListener,
    private val requestListener: RequestClickListener
) :
    RecyclerView.Adapter<FriendRequestViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendRequestViewHolder {
        return FriendRequestViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.one_friend_request, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FriendRequestViewHolder, position: Int) {
        val model = mData[position]
        Picasso.get().load(model.avatar).into(holder.mImageView)
        holder.mTextView.text = model.name

        holder.mTextView.setOnClickListener {
            listener.pressFriend(model.id)
        }
        holder.acceptImageButton.setOnClickListener {
            requestListener.accept(model.id)
        }
        holder.denyImageButton.setOnClickListener {
            requestListener.deny(model.id)
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    fun refresh(mData: MutableList<FriendModel>) {
        this.mData = mData
    }
}