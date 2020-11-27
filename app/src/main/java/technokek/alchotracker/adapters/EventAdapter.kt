package technokek.alchotracker.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import technokek.alchotracker.R
import technokek.alchotracker.adapters.viewholders.EventViewHolder
import technokek.alchotracker.api.EventClickListener
import technokek.alchotracker.data.models.EventModel

class EventAdapter(
    private val mData: MutableList<EventModel>,
    private val listener: EventClickListener
) :
    RecyclerView.Adapter<EventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        return EventViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.one_event, parent, false),
            listener
        )
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val model = mData[position]
        Picasso.get().load(model.avatar).into(holder.mImageView)
        holder.mTextView.text = model.name
    }

    override fun getItemCount(): Int {
        return mData.size
    }
}
