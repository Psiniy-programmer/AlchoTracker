package technokek.alchotracker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import technokek.alchotracker.R
import technokek.alchotracker.adapters.viewholders.FriendPreferencesViewHolder
import technokek.alchotracker.data.models.FriendPreferencesModel

class FriendPreferencesAdapter(private var mData: MutableList<FriendPreferencesModel>) :
    RecyclerView.Adapter<FriendPreferencesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendPreferencesViewHolder {
        return FriendPreferencesViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.friend_preference_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FriendPreferencesViewHolder, position: Int) {
        val model = mData[position]
        holder.mNameView.text = model.name
    }

    override fun getItemCount(): Int {
        return this.mData.size
    }

    fun refresh(mData: MutableList<FriendPreferencesModel>) {
        this.mData = mData
    }
}
