package technokek.alchotracker.calendar

import android.widget.EditText
import technokek.alchotracker.R
import java.time.YearMonth

private typealias Place = AlkoEvent.Place

fun generateAlkoEvents(): MutableList<AlkoEvent> {
    val list = mutableListOf<AlkoEvent>()
    val currentMonth = YearMonth.now()

    val currentMonth17 = currentMonth.atDay(17)
    list.add(AlkoEvent(currentMonth17.atTime(14, 0), Place("143", "Moscow"), R.color.brown_700))
    list.add(AlkoEvent(currentMonth17.atTime(21, 30), Place("437", "Kazan"), R.color.blue_grey_700))

    val currentMonth22 = currentMonth.atDay(22)
    list.add(AlkoEvent(currentMonth22.atTime(13, 20), Place("500", "Berlin"), R.color.blue_800))
    list.add(AlkoEvent(currentMonth22.atTime(17, 40), Place("1000", "New-York"), R.color.red_800))
    list.add(AlkoEvent(currentMonth22.atTime(20, 30), Place("110", "Brooklyn"), R.color.blue_800))

    list.add(
        AlkoEvent(
            currentMonth.atDay(3).atTime(20, 0),
            Place("200", "Sankt-Petersburg"),
            R.color.teal_700
        )
    )

    list.add(
        AlkoEvent(
            currentMonth.atDay(12).atTime(18, 15),
            Place("300", "Chelyabinsk"),
            R.color.cyan_700
        )
    )

    val nextMonth13 = currentMonth.plusMonths(1).atDay(13)
    list.add(AlkoEvent(nextMonth13.atTime(7, 30), Place("50", "Moscow"), R.color.pink_700))
    list.add(AlkoEvent(nextMonth13.atTime(10, 50), Place("450", "Moscow"), R.color.green_700))

    list.add(
        AlkoEvent(
            currentMonth.minusMonths(1).atDay(9).atTime(20, 15),
            Place("300", "Munich"),
            R.color.orange_800
        )
    )

    return list
}

fun isEmpty(eText: EditText): Boolean {
    if (eText.text.toString().trim().isNotEmpty())
        return false
    return true
}
