package technokek.alchotracker.adapters.viewholders

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import de.hdodenhof.circleimageview.CircleImageView
import technokek.alchotracker.R

class FriendRequestViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    var mImageView: CircleImageView = itemView.findViewById(R.id.friend_request_image)
        private set
    var mTextView: TextView = itemView.findViewById(R.id.friend_request_text)
        private set
    var acceptImageButton: MaterialButton = itemView.findViewById(R.id.accept_request)
        private set
    var denyImageButton: MaterialButton = itemView.findViewById(R.id.deny_request)
        private set
}
