package technokek.alchotracker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import technokek.alchotracker.R
import technokek.alchotracker.adapters.viewholders.SearchFriendViewModel
import technokek.alchotracker.api.FoundUserListener
import technokek.alchotracker.data.models.FriendModel
import technokek.alchotracker.data.models.SearchFriendModel

class SearchFriendAdapter(
    private var mData: MutableList<SearchFriendModel>,
    private val listener: FoundUserListener
) :
    RecyclerView.Adapter<SearchFriendViewModel>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchFriendViewModel {
        return SearchFriendViewModel(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.one_user_found, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SearchFriendViewModel, position: Int) {
        val model = mData[position]
        Picasso.get().load(model.avatar).into(holder.mImageView)
        holder.mTextView.text = model.name
        holder.mTextView.setOnClickListener {
            listener.pressUser(model.id)
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    fun refresh(mData: MutableList<SearchFriendModel>) {
        this.mData = mData
    }
}
