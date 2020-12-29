package technokek.alchotracker.adapters.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import technokek.alchotracker.R

class ChatListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var mImageView: ImageView = itemView.findViewById(R.id.chat_friend_image)
        private set
    var nameTextView: TextView = itemView.findViewById(R.id.chat_friend_name)
        private set
    var textTextView: TextView = itemView.findViewById(R.id.chat_friend_text)
        private set
    var timeTextView: TextView = itemView.findViewById(R.id.chat_friend_time)
        private set
    var lineLayout: LinearLayout = itemView.findViewById(R.id.linear_chat)
        private set
}
