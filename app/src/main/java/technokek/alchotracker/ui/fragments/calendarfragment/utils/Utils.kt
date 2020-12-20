package technokek.alchotracker.ui.fragments.calendarfragment.utils

import android.widget.EditText
import java.time.YearMonth
import technokek.alchotracker.R
import technokek.alchotracker.data.models.CalendarModel

private typealias Place = CalendarModel.Place


fun generateAlkoEvents(): MutableList<CalendarModel> {
    val list = mutableListOf<CalendarModel>()
    val currentMonth = YearMonth.now()

    val currentMonth17 = currentMonth.atDay(17)
    list.add(
        CalendarModel(
            currentMonth17.atTime(14, 0),
            Place("Moscow", "143", "Moscow"),
            R.color.brown_700,
            id = "1",
            adminId = "1"
        )
    )
    list.add(
        CalendarModel(
            currentMonth17.atTime(21, 30),
            Place("Moscow", "437", "Kazan"),
            R.color.blue_grey_700,
            id = "1",
            adminId = "1"
        )
    )

    val currentMonth22 = currentMonth.atDay(22)
    list.add(
        CalendarModel(
            currentMonth22.atTime(13, 20),
            Place("Moscow", "500", "Berlin"),
            R.color.blue_800,
            id = "1",
            adminId = "1"
        )
    )
    list.add(
        CalendarModel(
            currentMonth22.atTime(17, 40),
            Place("Moscow", "1000", "New-York"),
            R.color.red_800,
            id = "1",
            adminId = "1"
        )
    )
    list.add(
        CalendarModel(
            currentMonth22.atTime(20, 30),
            Place("Moscow", "110", "Brooklyn"),
            R.color.blue_800,
            id = "1",
            adminId = "1"
        )
    )

    list.add(
        CalendarModel(
            currentMonth.atDay(3).atTime(20, 0),
            Place("Moscow", "200", "Sankt-Petersburg"),
            R.color.teal_700,
            id = "1",
            adminId = "1"
        )
    )

    list.add(
        CalendarModel(
            currentMonth.atDay(12).atTime(18, 15),
            Place("Moscow", "300", "Chelyabinsk"),
            R.color.cyan_700,
            id = "1",
            adminId = "1"
        )
    )

    val nextMonth13 = currentMonth.plusMonths(1).atDay(13)
    list.add(
        CalendarModel(
            nextMonth13.atTime(7, 30),
            Place("Moscow", "50", "Moscow"),
            R.color.pink_700,
            id = "1",
            adminId = "1"
        )
    )
    list.add(
        CalendarModel(
            nextMonth13.atTime(10, 50),
            Place("Moscow", "450", "Moscow"),
            R.color.green_700,
            id = "1",
            adminId = "1"
        )
    )

    list.add(
        CalendarModel(
            currentMonth.minusMonths(1).atDay(9).atTime(20, 15),
            Place("Moscow", "300", "Munich"),
            R.color.orange_800,
            id = "1",
            adminId = "1"
        )
    )

    return list
}

fun isEmpty(eText: EditText): Boolean {
    if (eText.text.toString().trim().isNotEmpty())
        return false
    return true
}

