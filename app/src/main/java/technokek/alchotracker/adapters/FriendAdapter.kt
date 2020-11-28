package technokek.alchotracker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import technokek.alchotracker.R
import technokek.alchotracker.adapters.viewholders.FriendViewHolder
import technokek.alchotracker.api.FriendClickListener
import technokek.alchotracker.data.models.FriendModel

class FriendAdapter(
    private val mData: MutableList<FriendModel>,
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
            listener.pressFriend(model.id)
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }
}
