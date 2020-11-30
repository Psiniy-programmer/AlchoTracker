package technokek.alchotracker.adapters.viewholders

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import technokek.alchotracker.R

class FriendRequestViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    var mImageView: ImageView = itemView.findViewById(R.id.friend_request_image)
        private set
    var mTextView: TextView = itemView.findViewById(R.id.friend_request_text)
        private set
    var acceptImageButton: ImageButton = itemView.findViewById(R.id.accept_request)
        private set
    var denyImageButton: ImageButton = itemView.findViewById(R.id.deny_request)
        private set
}