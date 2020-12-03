package technokek.alchotracker.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate
import technokek.alchotracker.adapters.viewholders.AlkoEventsViewHolder
import technokek.alchotracker.data.models.CalendarModel
import technokek.alchotracker.databinding.AlkoEventItemViewBinding
import technokek.alchotracker.ui.fragments.calendarfragment.utils.layoutInflater

class AlkoEventsAdapter(
    private val events: MutableList<CalendarModel> = mutableListOf(),
    private val actionListener: ActionListener
) : RecyclerView.Adapter<AlkoEventsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlkoEventsViewHolder {
        return AlkoEventsViewHolder(
            AlkoEventItemViewBinding.inflate(parent.context.layoutInflater, parent, false),
            actionListener = actionListener
        )
    }

    override fun onBindViewHolder(viewHolder: AlkoEventsViewHolder, position: Int) {
        viewHolder.bind(events[position])
    }

    override fun getItemCount(): Int = events.size

    fun refresh(date: LocalDate?, alkoEvents: MutableMap<LocalDate, MutableList<CalendarModel>>) {
        this.events.clear()
        this.events.addAll(alkoEvents[date].orEmpty())
    }

    interface ActionListener {
        fun onEventClick(calendarModel: CalendarModel)
    }
}
