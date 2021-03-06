package technokek.alchotracker.adapters.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import technokek.alchotracker.R

class ChatCreateDialogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var mImageView: ImageView = itemView.findViewById(R.id.friend_image)
        private set
    var mTextView: TextView = itemView.findViewById(R.id.friend_text)
        private set
    var relativeLayout: RelativeLayout = itemView.findViewById(R.id.relative_friend)
        private set
}
