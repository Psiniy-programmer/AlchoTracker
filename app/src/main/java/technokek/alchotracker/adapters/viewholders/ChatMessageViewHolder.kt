package technokek.alchotracker.adapters.viewholders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import technokek.alchotracker.R

class ChatMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var mMessage: TextView = itemView.findViewById(R.id.chat_text)
        private set
    var mDate: TextView = itemView.findViewById(R.id.chat_date)
        private set
}