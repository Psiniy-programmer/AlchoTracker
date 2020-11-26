package technokek.alchotracker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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
                .inflate(R.layout.one_friend, parent, false),
            listener
        )
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        val model = mData[position]
        holder.mImageView.setImageBitmap(model.avatar)
        holder.mTextView.text = model.name
    }

    override fun getItemCount(): Int {
        return mData.size
    }
}
