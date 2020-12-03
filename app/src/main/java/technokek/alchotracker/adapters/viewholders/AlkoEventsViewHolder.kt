package technokek.alchotracker.adapters.viewholders

import androidx.recyclerview.widget.RecyclerView
import java.time.format.DateTimeFormatter
import technokek.alchotracker.adapters.AlkoEventsAdapter
import technokek.alchotracker.data.models.CalendarModel
import technokek.alchotracker.databinding.AlkoEventItemViewBinding
import technokek.alchotracker.ui.fragments.calendarfragment.utils.getColorCompat

class AlkoEventsViewHolder(
    val binding: AlkoEventItemViewBinding,
    private val actionListener: AlkoEventsAdapter.ActionListener
) :
    RecyclerView.ViewHolder(binding.root) {
    private val formatter = DateTimeFormatter.ofPattern("EEE'\n'dd MMM'\n'HH:mm")

    fun bind(calendarModel: CalendarModel) {
        binding.itemAlkoEventDateText.apply {
            text = formatter.format(calendarModel.time)
            setBackgroundColor(itemView.context.getColorCompat(calendarModel.color))
        }

        binding.itemAlkoEventCodeText.text = calendarModel.eventPlace.place
        binding.itemAlkoEventPlaceCityText.text = calendarModel.eventPlace.price
        binding.alkoEventItem.setOnClickListener {
            actionListener.onEventClick(calendarModel)
        }
    }
}
