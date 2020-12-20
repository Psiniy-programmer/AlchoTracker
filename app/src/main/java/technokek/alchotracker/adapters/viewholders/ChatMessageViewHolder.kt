package technokek.alchotracker.adapters.viewholders

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import technokek.alchotracker.R

class ChatMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var mMasterLayout: LinearLayout = itemView.findViewById(R.id.master_chat_layout)
        private set
    var mMasterMessage: TextView = itemView.findViewById(R.id.master_chat_text)
        private set
    var mMasterDate: TextView = itemView.findViewById(R.id.master_chat_date)
        private set
    var mFriendLayout: LinearLayout = itemView.findViewById(R.id.friend_chat_layout)
        private set
    var mFriendMessage: TextView = itemView.findViewById(R.id.friend_chat_text)
        private set
    var mFriendDate: TextView = itemView.findViewById(R.id.friend_chat_date)
        private set
}