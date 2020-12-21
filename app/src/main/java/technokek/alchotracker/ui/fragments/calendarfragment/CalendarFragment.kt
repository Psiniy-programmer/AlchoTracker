package technokek.alchotracker.ui.fragments.calendarfragment

import android.app.AlarmManager
import android.app.Dialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.kizitonwose.calendarview.utils.next
import com.kizitonwose.calendarview.utils.previous
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*
import technokek.alchotracker.R
import technokek.alchotracker.adapters.AlkoEventsAdapter
import technokek.alchotracker.api.SharedPreferencesHolder
import technokek.alchotracker.data.models.CalendarModel
import technokek.alchotracker.databinding.*
import technokek.alchotracker.receivers.AlertReceiver
import technokek.alchotracker.ui.fragments.calendarfragment.utils.*
import technokek.alchotracker.ui.fragments.calendarfragment.utils.setTextColorRes
import technokek.alchotracker.viewmodels.CalendarViewModel
import technokek.alchotracker.data.Constants.*

class CalendarFragment : Fragment(R.layout.calendar_fragment), AlkoEventsAdapter.ActionListener {

    private var selectedDate: LocalDate? = null
    private var selectedCalendarModel: CalendarModel? = null
    private val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")

    private lateinit var alkoEventsAdapter: AlkoEventsAdapter
    // Statically generated events
    /*private var alkoEvents: MutableMap<LocalDate, MutableList<CalendarModel>> =
        generateAlkoEvents().groupBy { it.time.toLocalDate() } as MutableMap<LocalDate, MutableList<CalendarModel>>*/
    private lateinit var mCalendarViewModel: CalendarViewModel
    private lateinit var binding: CalendarFragmentBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var alarmManager: AlarmManager
    private lateinit var mAuth: FirebaseAuth

    private lateinit var dialog: Dialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        sharedPreferences = (activity as SharedPreferencesHolder).sharedPreferences
        editor = sharedPreferences.edit()
        alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        // set ViewModel
        mCalendarViewModel = ViewModelProvider(this)[CalendarViewModel::class.java]
        alkoEventsAdapter = AlkoEventsAdapter(actionListener = this)
        binding = CalendarFragmentBinding.bind(view)
        mCalendarViewModel.mMediatorLiveData.observe(
            viewLifecycleOwner,
            {
                updateAdapterForDate(selectedDate)
                binding.calendarFragmentCalendar.notifyCalendarChanged()
            }
        )

        binding.calendarRv.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = alkoEventsAdapter
            addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
        }
        alkoEventsAdapter.notifyDataSetChanged()

        val daysOfWeek = daysOfWeekFromLocale()

        val currentMonth = YearMonth.now()
        binding.calendarFragmentCalendar.setup(
            currentMonth.minusMonths(10),
            currentMonth.plusMonths(10),
            daysOfWeek.first()
        )
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
                            // binding.buttonDelete.isEnabled = alkoEvents[day.date] != null
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
                    layout.setBackgroundResource(
                        if (selectedDate == day.date) R.drawable.calendar_selected_bg else 0
                    )

                    var dayEvents: MutableList<CalendarModel>? = null
                    if (mCalendarViewModel.mMediatorLiveData.value?.get(day.date) != null) {
                        dayEvents = mCalendarViewModel.mMediatorLiveData.value!![day.date]
                    }
                    if (dayEvents != null) {
                        if (dayEvents.count() == 1) {
                            alkoEventBottomView.setBackgroundColor(
                                view.context.getColorCompat(
                                    dayEvents[0].color
                                )
                            )
                        } else {
                            alkoEventTopView.setBackgroundColor(
                                view.context.getColorCompat(
                                    dayEvents[0].color
                                )
                            )
                            alkoEventBottomView.setBackgroundColor(
                                view.context.getColorCompat(
                                    dayEvents[1].color
                                )
                            )
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
        binding.calendarFragmentCalendar.monthHeaderBinder =
            object : MonthHeaderFooterBinder<MonthViewContainer> {
                override fun create(view: View) = MonthViewContainer(view)
                override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                    // Setup each header day text if we have not done that already.
                    if (container.legendLayout.tag == null) {
                        container.legendLayout.tag = month.yearMonth
                        container.legendLayout.children.map { it as TextView }
                            .forEachIndexed { index, tv ->
                                tv.text = daysOfWeek[index].getDisplayName(
                                    TextStyle.SHORT,
                                    Locale.ENGLISH
                                )
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

        // pop up dialog binding
        binding.buttonAdd.setOnClickListener {
            showPopUp(selectedDate)
        }
        // button delete logic
        binding.buttonDelete.isEnabled = false
        binding.buttonDelete.setOnClickListener {
            deleteEvent(selectedDate, selectedCalendarModel)
        }
    }

    private fun updateAdapterForDate(date: LocalDate?) {
        alkoEventsAdapter.refresh(date, mCalendarViewModel.mMediatorLiveData.value!!)
        alkoEventsAdapter.notifyDataSetChanged()
    }

    private fun showPopUp(date: LocalDate?) {
        dialog = Dialog(this.requireContext())
        dialog.setContentView(R.layout.popup_add_menu)
        val buttonClose = dialog.findViewById<Button>(R.id.button_close)
        lateinit var time: LocalDateTime
        val etEventName = dialog.findViewById<EditText>(R.id.pop_up_place)
        val etEventCosts = dialog.findViewById<EditText>(R.id.pop_up_costs)
        val openTimePicker = dialog.findViewById<Button>(R.id.pop_up_time)
        var openTimePickerClicked = false
        val buttonSubmit = dialog.findViewById<Button>(R.id.button_submit)

        buttonClose!!.setOnClickListener {
            dialog.dismiss()
        }

        openTimePicker.setOnClickListener {
            val timePickerDialog = TimePickerDialog(
                this.activity,
                android.R.style.Theme_Holo_Light_DarkActionBar,
                { view, hourOfDay, minute ->
                    time = date!!.atTime(hourOfDay, minute)
                    openTimePickerClicked = true
                    buttonSubmit!!.isEnabled =
                        !(isEmpty(etEventName) && isEmpty(etEventCosts) && !openTimePickerClicked)
                },
                0,
                0,
                true
            )
            timePickerDialog.window?.attributes!!.gravity = requireActivity().window.attributes.gravity
            //timePickerDialog.window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
            timePickerDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            // TODO fix this crash
            // timePickerDialog.updateTime(time.hour, time.minute)
            timePickerDialog.show()
        }
        // button submit logic
        buttonSubmit.isEnabled = false
        buttonSubmit.setOnClickListener {
            if (isEmpty(etEventName) || isEmpty(etEventCosts)) {
                Toast.makeText(this.context, "Fill in all the fields!", Toast.LENGTH_LONG).show()
            } else {
                val newAlkoEvent = formAlkoEvent(
                    etEventName,
                    etEventCosts,
                    time
                )
                // Прокидываем в VM
                mCalendarViewModel.pushData(date!!, newAlkoEvent)
                //TODO добавить Notification
                getCalendarAndStartAlarm(newAlkoEvent.time)
                updateAdapterForDate(date)
                dialog.dismiss()
            }
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun formAlkoEvent(
        eventName: EditText,
        eventCosts: EditText,
        eventTime: LocalDateTime
    ): CalendarModel {
        val place = eventName.text.toString()
        val costs = eventCosts.text.toString()
        // TODO should have ask name of the event also
        Log.d(
            "ValueSize",
            mCalendarViewModel.mMediatorLiveData.value!!
                .values.flatMap { it.toList() }.size.toString()
        )
        return CalendarModel(
            eventTime,
            PlaceLoc(place, costs, place),
            R.color.teal_700,
            adminId = mAuth.currentUser?.uid.toString(),
            id = (mCalendarViewModel.mMediatorLiveData.value!!
                .values.flatMap { it.toList() }.size + 1).toString()
        )
    }

    private fun deleteEvent(date: LocalDate?, calendarModel: CalendarModel?) {
        // Прокидываем в VM
        // TODO нужно сделать callback для отключения кнопки
        if (calendarModel == null || selectedDate != date) {
            Toast.makeText(this.context, "You didnt choose the event!", Toast.LENGTH_LONG).show()
            return
        }
        try {
            if (mCalendarViewModel.mMediatorLiveData.value!![date]!!.isNotEmpty()) {
                mCalendarViewModel.pushDeleteEvent(date!!, calendarModel)
                //Delete from SP

                deleteTimestampFromSP(calendarModel.time)
            }
        } catch (e: Exception) {
            Toast.makeText(this.context, "Nothing to delete!", Toast.LENGTH_LONG).show()
        }
        /*else {
            binding.buttonDelete.isEnabled = false
        }*/
        updateAdapterForDate(date)
    }

    private fun getCalendarAndStartAlarm(timeStart: LocalDateTime) {
        val calendar = Calendar.getInstance()
        calendar.set(
            timeStart.year,
            timeStart.monthValue - 1,
            timeStart.dayOfMonth,
            timeStart.hour,
            timeStart.minute
        )
        calendar.set(Calendar.SECOND, 0)
        //startAlarm
        startAlarm(calendar, timeStart)
    }

    private fun startAlarm(calendar: Calendar, timeStart: LocalDateTime) {
        val intent: Intent = Intent(context, AlertReceiver::class.java)
        val requestCode: Int = getRequestCode()
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_ONE_SHOT
        )
        //save in sp
        saveTimestampInSP(timeStart, requestCode)
        Log.d("SPAfterSave", sharedPreferences.getStringSet(TIMER_TIMESTAMPS_IN_SP, mutableSetOf()).toString())
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }

    private fun saveTimestampInSP(timeStart: LocalDateTime, requestCode: Int) {
        val now = LocalDateTime.now()
        if (timeStart.isAfter(now)) {
            val timestamps = sharedPreferences.getStringSet(
                TIMER_TIMESTAMPS_IN_SP, mutableSetOf())
            val ed = sharedPreferences.edit()
            Log.d("SPBeforeSave", timestamps.toString())
            timestamps!!.add("$timeStart;$requestCode")
            ed.clear()
            ed.putStringSet(TIMER_TIMESTAMPS_IN_SP, timestamps).apply()
            //Log.d("SPAfterSave", timestamps.toString())
        }
    }

    private fun deleteTimestampFromSP(timeStart: LocalDateTime) {
        val now = LocalDateTime.now()
        if (timeStart.isAfter(now)) {
            val timestamps = sharedPreferences.getStringSet(
                TIMER_TIMESTAMPS_IN_SP, mutableSetOf())
            Log.d("SPBeforeDelete", timestamps.toString())
            val ed = sharedPreferences.edit()
            CoroutineScope(Dispatchers.IO).launch {
                val parts = timestamps!!.find {
                    it.contains(timeStart.toString())
                }?.split(";")
                //remove timestamp
                timestamps.removeIf {
                    try {
                        it.contains(parts!![0])
                    }
                    catch (e: Exception) {
                        Log.d("Parts", parts.toString())
                        false
                    }
                }
                ed.clear()
                ed.putStringSet(TIMER_TIMESTAMPS_IN_SP, timestamps).apply()

                //Cancel alarm
                try {
                    cancelAlarm(parts!![1].toInt())
                }
                catch (e: Exception) {
                    Log.d("Cancel null", "cancelAlarmReceived null")
                }
            }

            Log.d("SPAfterDelete", timestamps.toString())
        }
    }


    override fun onEventClick(calendarModel: CalendarModel) {
        selectedCalendarModel = calendarModel
        binding.buttonDelete.isEnabled = true
    }

    override fun onAcceptClick(calendarModel: CalendarModel) {
        //TODO
        mCalendarViewModel.onMemberAccepted(calendarModel)
    }

    override fun onDenyClick(calendarModel: CalendarModel) {
        mCalendarViewModel.onMemberDenied(calendarModel)
    }

    private fun cancelAlarm(requestCode: Int) {
        val intent = Intent(context, AlertReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_ONE_SHOT
        )
        alarmManager.cancel(pendingIntent)
    }

    companion object {
        fun getRequestCode(): Int {
            val random = Random()
            //request code is null based in my shared preferences
            /*val timestamps = sharedPreferences.getStringSet(
                TIMER_TIMESTAMPS_IN_SP, mutableSetOf())*/
            return random.nextInt(100000)
        }
    }


}

private typealias PlaceLoc = CalendarModel.Place
