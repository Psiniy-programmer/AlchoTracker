package technokek.alchotracker.adapters.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import technokek.alchotracker.R

class SearchFriendViewModel(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var mImageView: ImageView = itemView.findViewById(R.id.found_user_image)
        private set
    var mTextView: TextView = itemView.findViewById(R.id.found_user_text)
        private set
}
