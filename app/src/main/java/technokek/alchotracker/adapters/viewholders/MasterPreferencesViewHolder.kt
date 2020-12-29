package technokek.alchotracker.adapters.viewholders

import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import technokek.alchotracker.R

class MasterPreferencesViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    var mNameView: MaterialTextView = itemView.findViewById(R.id.master_preferences_item_text)
        private set
    var mDeleteView: MaterialButton = itemView.findViewById(R.id.master_preferences_item_btn)
        private set
}
