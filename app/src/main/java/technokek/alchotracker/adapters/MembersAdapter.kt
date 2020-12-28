package technokek.alchotracker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import technokek.alchotracker.R
import technokek.alchotracker.adapters.viewholders.FriendViewHolder
import technokek.alchotracker.api.FriendToFriendClickListener
import technokek.alchotracker.data.models.FriendModel

class MembersAdapter(
    private var mData: MutableList<FriendModel>,
    private val listener: FriendToFriendClickListener
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
            listener.pressMember(model.id)
        }
        holder.mRelativeLayout.setOnClickListener {
            listener.pressMember(model.id)
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    fun refresh(mData: MutableList<FriendModel>) {
        this.mData = mData
    }
}