package adapters.viewholders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.profile_preferences_item.view.*
import technokek.alchotracker.R

class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var mTextItem: TextView = itemView.findViewById(R.id.profile_preferences_item_text)
        private set
}