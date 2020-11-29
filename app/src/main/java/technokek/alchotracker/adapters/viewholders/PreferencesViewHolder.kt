package technokek.alchotracker.adapters.viewholders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import technokek.alchotracker.R

class PreferencesViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
        var mNameView: TextView = itemView.findViewById(R.id.profile_preferences_item)
            private set
}