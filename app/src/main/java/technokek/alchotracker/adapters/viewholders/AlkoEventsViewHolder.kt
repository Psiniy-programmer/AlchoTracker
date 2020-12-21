package technokek.alchotracker.adapters.viewholders

import android.opengl.Visibility
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
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
    private val mAuth = FirebaseAuth.getInstance()
    private var buttonAcceptClicked = false
    private var buttonDenyClicked = false

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

        val currentUser = mAuth.currentUser?.uid.toString()
        Log.d("Condition:", (calendarModel.adminId != currentUser && !calendarModel.userClicked).toString())
        Log.d("Condition1:", (calendarModel.adminId != currentUser).toString())
        Log.d("Condition2:", (!calendarModel.userClicked).toString())

        if (binding.acceptRequest.visibility == View.VISIBLE || binding.denyRequest.visibility == View.VISIBLE) {
            binding.acceptRequest.visibility = View.GONE
            binding.denyRequest.visibility = View.GONE
        }

        if (calendarModel.adminId != currentUser && !(buttonAcceptClicked || buttonDenyClicked)) {
            calendarModel.userClicked = true
            binding.acceptRequest.apply {
                visibility = View.VISIBLE
                isClickable = true
                setOnClickListener {
                    actionListener.onAcceptClick(calendarModel)
                    buttonAcceptClicked = true
                    visibility = View.GONE
                    isClickable = false
                    binding.denyRequest.visibility = View.GONE
                    binding.denyRequest.isClickable = false
                }
            }
            binding.denyRequest.apply {
                visibility = View.VISIBLE
                isClickable = true
                setOnClickListener {
                    actionListener.onDenyClick(calendarModel)
                    buttonDenyClicked = true
                    visibility = View.GONE
                    isClickable = false
                    binding.acceptRequest.visibility = View.GONE
                    binding.acceptRequest.isClickable = false
                }
            }
        }
    }
}
