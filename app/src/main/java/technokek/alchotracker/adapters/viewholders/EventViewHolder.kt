package technokek.alchotracker.adapters.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import technokek.alchotracker.R
import technokek.alchotracker.api.EventClickListener

class EventViewHolder(itemView: View, listener: EventClickListener) :
    RecyclerView.ViewHolder(itemView) {

    var mImageView: ImageView = itemView.findViewById(R.id.event_image)
        private set
    var mTextView: TextView = itemView.findViewById(R.id.event_text)
        private set

    init {
        mTextView.setOnClickListener {
            listener.pressEvent()
        }
    }
}
