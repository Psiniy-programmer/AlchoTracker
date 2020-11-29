package technokek.alchotracker.calendar

import android.app.Dialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.appcompat.widget.Toolbar
import androidx.core.view.children
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.kizitonwose.calendarview.utils.next
import com.kizitonwose.calendarview.utils.previous
import technokek.alchotracker.R
import technokek.alchotracker.databinding.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

/*
Дата класс, который описывает события и их место
 */
data class AlkoEvent(val time: LocalDateTime, val eventPlace: Place, @ColorRes val color: Int) {
    data class Place(val costs: String, val place: String)
}

class AlkoEventsAdapter(private val actionListener: ActionListener) : RecyclerView.Adapter<AlkoEventsAdapter.AlkoEventsViewHolder>() {

    val events = mutableListOf<AlkoEvent>()

    private val formatter = DateTimeFormatter.ofPattern("EEE'\n'dd MMM'\n'HH:mm")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlkoEventsViewHolder {
        return AlkoEventsViewHolder(
            AlkoEventItemViewBinding.inflate(parent.context.layoutInflater, parent, false),
            actionListener = actionListener
        )
    }

    override fun onBindViewHolder(viewHolder: AlkoEventsViewHolder, position: Int) {
        viewHolder.bind(events[position])
    }

    interface ActionListener {
        fun onEventClick(alkoEvent: AlkoEvent)
    }

    override fun getItemCount(): Int = events.size

    inner class AlkoEventsViewHolder(val binding: AlkoEventItemViewBinding, private val actionListener: ActionListener) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(alkoEvent: AlkoEvent) {
            binding.itemAlkoEventDateText.apply {
                text = formatter.format(alkoEvent.time)
                setBackgroundColor(itemView.context.getColorCompat(alkoEvent.color))
            }

            binding.itemAlkoEventCodeText.text = alkoEvent.eventPlace.place
            binding.itemAlkoEventPlaceCityText.text = alkoEvent.eventPlace.costs
            binding.alkoEventItem.setOnClickListener {
                actionListener.onEventClick(alkoEvent)
            }
        }
    }
}

class CalendarFragment : BaseFragment(R.layout.calendar_fragment), HasToolbar, AlkoEventsAdapter.ActionListener {

    override val toolbar: Toolbar?
        get() = null

    override val titleRes: Int = R.string.calendar_title

    private var selectedDate: LocalDate? = null
    private var selectedAlkoEvent: AlkoEvent? = null
    private val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")

    private val alkoEventsAdapter = AlkoEventsAdapter(this)
    private var alkoEvents: MutableMap<LocalDate, MutableList<AlkoEvent>> = generateAlkoEvents().groupBy { it.time.toLocalDate() } as MutableMap<LocalDate, MutableList<AlkoEvent>>

    private lateinit var binding: CalendarFragmentBinding

    private lateinit var dialog: Dialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = CalendarFragmentBinding.bind(view)

        binding.calendarRv.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = alkoEventsAdapter
            addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
        }
        alkoEventsAdapter.notifyDataSetChanged()

        val daysOfWeek = daysOfWeekFromLocale()

        val currentMonth = YearMonth.now()
        binding.calendarFragmentCalendar.setup(currentMonth.minusMonths(10), currentMonth.plusMonths(10), daysOfWeek.first())
        binding.calendarFragmentCalendar.scrollToMonth(currentMonth)

        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay // Will be set when this container is bound.
            val binding = CalendarDayBinding.bind(view)
            init {
                view.setOnClickListener {
                    if (day.owner == DayOwner.THIS_MONTH) {
                        if (selectedDate != day.date) {
                            val oldDate = selectedDate
                            selectedDate = day.date
                            val binding = this@CalendarFragment.binding
                            binding.calendarFragmentCalendar.notifyDateChanged(day.date)
                            binding.buttonAdd.isEnabled = true
                            //binding.buttonDelete.isEnabled = alkoEvents[day.date] != null
                            oldDate?.let { binding.calendarFragmentCalendar.notifyDateChanged(it) }
                            updateAdapterForDate(day.date)
                        }
                    }
                }
            }
        }
        binding.calendarFragmentCalendar.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.binding.calendarDayDayText
                val layout = container.binding.calendarDayLayout
                textView.text = day.date.dayOfMonth.toString()

                val alkoEventTopView = container.binding.calendarDayAlkoEventTop
                val alkoEventBottomView = container.binding.calendarDayAlkoEventBottom
                alkoEventTopView.background = null
                alkoEventBottomView.background = null

                if (day.owner == DayOwner.THIS_MONTH) {
                    textView.setTextColorRes(R.color.calendar_text_grey)
                    layout.setBackgroundResource(if (selectedDate == day.date) R.drawable.calendar_selected_bg else 0)

                    val dayEvents = alkoEvents[day.date]
                    if (dayEvents != null) {
                        if (dayEvents.count() == 1) {
                            alkoEventBottomView.setBackgroundColor(view.context.getColorCompat(dayEvents[0].color))
                        } else {
                            alkoEventTopView.setBackgroundColor(view.context.getColorCompat(dayEvents[0].color))
                            alkoEventBottomView.setBackgroundColor(view.context.getColorCompat(dayEvents[1].color))
                        }
                    }
                } else {
                    textView.setTextColorRes(R.color.calendar_text_grey_light)
                    layout.background = null
                }
            }
        }

        class MonthViewContainer(view: View) : ViewContainer(view) {
            val legendLayout = CalendarHeaderBinding.bind(view).legendLayout.root
        }
        binding.calendarFragmentCalendar.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                // Setup each header day text if we have not done that already.
                if (container.legendLayout.tag == null) {
                    container.legendLayout.tag = month.yearMonth
                    container.legendLayout.children.map { it as TextView }.forEachIndexed { index, tv ->
                        tv.text = daysOfWeek[index].getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                            .toUpperCase(Locale.ENGLISH)
                        tv.setTextColorRes(R.color.calendar_text_grey)
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                    }
                    month.yearMonth
                }
            }
        }

        binding.calendarFragmentCalendar.monthScrollListener = { month ->
            val title = "${monthTitleFormatter.format(month.yearMonth)} ${month.yearMonth.year}"
            binding.calendarMonthYearText.text = title

            selectedDate?.let {
                // Clear selection if we scroll to a new month.
                selectedDate = null
                binding.calendarFragmentCalendar.notifyDateChanged(it)
                binding.buttonAdd.isEnabled = false
                binding.buttonDelete.isEnabled = false
                updateAdapterForDate(null)
            }
        }

        binding.calendarNextMonthImage.setOnClickListener {
            binding.calendarFragmentCalendar.findFirstVisibleMonth()?.let {
                binding.calendarFragmentCalendar.smoothScrollToMonth(it.yearMonth.next)
            }
        }

        binding.calendarPreviousMonthImage.setOnClickListener {
            binding.calendarFragmentCalendar.findFirstVisibleMonth()?.let {
                binding.calendarFragmentCalendar.smoothScrollToMonth(it.yearMonth.previous)
            }
        }
        //pop up dialog binding
        binding.buttonAdd.setOnClickListener {
            showPopUp(selectedDate)
        }
        //button delete logic
        binding.buttonDelete.isEnabled = false
        binding.buttonDelete.setOnClickListener {
            //TODO delete one event
            deleteEvent(selectedDate, selectedAlkoEvent)
        }
    }

    override fun onStart() {
        super.onStart()
        requireActivity().window.statusBarColor = requireContext().getColorCompat(R.color.calendar_toolbar_color)
    }

    override fun onStop() {
        super.onStop()
        requireActivity().window.statusBarColor = requireContext().getColorCompat(R.color.colorPrimaryDark)
    }

    private fun updateAdapterForDate(date: LocalDate?) {
        alkoEventsAdapter.events.clear()
        alkoEventsAdapter.events.addAll(alkoEvents[date].orEmpty())
        alkoEventsAdapter.notifyDataSetChanged()
    }

    private fun showPopUp(date: LocalDate?) {
        dialog = Dialog(this.requireContext())
        dialog.setContentView(R.layout.popup_add_menu)
        val buttonClose = dialog.findViewById<Button>(R.id.button_close)
        buttonClose!!.setOnClickListener {
            dialog.dismiss()
        }
        lateinit var time: LocalDateTime
        val etEventName = dialog.findViewById<EditText>(R.id.pop_up_place)
        val etEventCosts = dialog.findViewById<EditText>(R.id.pop_up_costs)
        val openTimePicker = dialog.findViewById<Button>(R.id.pop_up_time)
        var openTimePickerClicked = false
        val buttonSubmit = dialog.findViewById<Button>(R.id.button_submit)
        openTimePicker.setOnClickListener {
            val timePickerDialog = TimePickerDialog(
                this.activity,
                android.R.style.Theme_Holo_Light_DarkActionBar,
                { view, hourOfDay, minute ->
                    time = date!!.atTime(hourOfDay, minute)
                    openTimePickerClicked = true
                    buttonSubmit!!.isEnabled = !(isEmpty(etEventName) && isEmpty(etEventCosts) && !openTimePickerClicked)
                }, 0, 0, true
            )
            timePickerDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            //TODO fix this crash
            //timePickerDialog.updateTime(time.hour, time.minute)
            timePickerDialog.show()
        }
        //button submit logic
        buttonSubmit.isEnabled = false
        buttonSubmit.setOnClickListener {
            if (isEmpty(etEventName) || isEmpty(etEventCosts)) {
                Toast.makeText(this.context, "Fill in all the fields!", Toast.LENGTH_LONG).show()
            }
            else {
                val newAlkoEvent = formAlkoEvent(
                    etEventName,
                    etEventCosts,
                    time
                )
                if (!alkoEvents.containsKey(date)) {
                    alkoEvents.put(date!!, mutableListOf(newAlkoEvent)
                    )
                }
                else {
                    val eventsThisDate = alkoEvents[date]
                    eventsThisDate!!.add(newAlkoEvent)
                    alkoEvents.put(date!!, eventsThisDate)
                }
                //TODO renew DB
                updateAdapterForDate(date)
                dialog.dismiss()
            }
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun formAlkoEvent(eventName: EditText,
                              eventCosts: EditText,
                              eventTime: LocalDateTime): AlkoEvent {
        val place = eventName.text.toString()
        val costs = eventCosts.text.toString()
        return AlkoEvent(
            eventTime,
            PlaceLoc(costs, place),
            R.color.teal_700
        )
    }

    private fun deleteEvent(date: LocalDate?, alkoEvent: AlkoEvent?) {
        var thisDayAlkoEvents = alkoEvents.remove(date)
        thisDayAlkoEvents!!.remove(alkoEvent)
        if (thisDayAlkoEvents.isNotEmpty()) {
            alkoEvents.put(date!!, thisDayAlkoEvents)
        }
        else {
            //TODO this cant be here with MVVM cos its UI
            binding.buttonDelete.isEnabled = false
        }
        //TODO renew DB
        updateAdapterForDate(date)
    }

    override fun onEventClick(alkoEvent: AlkoEvent) {
        selectedAlkoEvent = alkoEvent
        binding.buttonDelete.isEnabled = true
        //Toast.makeText(this.context, "Alko event selected!", Toast.LENGTH_LONG).show()
    }
}

private typealias PlaceLoc = AlkoEvent.Place
