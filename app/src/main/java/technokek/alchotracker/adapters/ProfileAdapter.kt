package technokek.alchotracker.adapters

import technokek.alchotracker.adapters.viewholders.ProfileViewHolder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import technokek.alchotracker.R

class ProfileAdapter(private val mData: MutableSet<String>) : RecyclerView.Adapter<ProfileViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        return ProfileViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.profile_preferences_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val model = mData.elementAt(position)
        holder.mTextItem.text = model
    }

    override fun getItemCount(): Int {
        return mData.size
    }
}