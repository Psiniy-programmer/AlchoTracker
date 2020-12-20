package technokek.alchotracker.adapters.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import technokek.alchotracker.R

class AlchooViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var mAvatar: ImageView = itemView.findViewById(R.id.card_avatar)
        private set
    var mName: TextView = itemView.findViewById(R.id.card_name)
        private set
    var mStatus: TextView = itemView.findViewById(R.id.card_status)
        private set
    var mFavouriteDrink: TextView = itemView.findViewById(R.id.card_favourite_drink)
        private set
}