package technokek.alchotracker.adapters.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView
import technokek.alchotracker.R

class FriendViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    var mImageView: CircleImageView = itemView.findViewById(R.id.friend_image)
        private set
    var mTextView: TextView = itemView.findViewById(R.id.friend_text)
        private set
}
