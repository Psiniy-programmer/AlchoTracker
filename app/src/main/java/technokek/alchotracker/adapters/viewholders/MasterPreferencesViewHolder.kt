package technokek.alchotracker.adapters.viewholders

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import technokek.alchotracker.R

class MasterPreferencesViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    var mNameView: TextView = itemView.findViewById(R.id.master_preferences_item_text)
        private set
    var mDeleteView: Button = itemView.findViewById(R.id.master_preferences_item_btn)
        private set
}
