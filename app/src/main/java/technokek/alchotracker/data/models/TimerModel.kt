package technokek.alchotracker.data.models

import java.time.LocalDateTime

data class TimerModel(
    val timeStart: LocalDateTime
) {
    override fun toString(): String {
        return "TimerModel(timeStart=$timeStart)"
    }
}