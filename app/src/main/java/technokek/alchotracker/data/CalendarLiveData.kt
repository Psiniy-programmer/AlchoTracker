package technokek.alchotracker.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.lang.Exception
import java.time.LocalDate
import java.time.LocalDateTime
import technokek.alchotracker.R
import technokek.alchotracker.data.models.CalendarModel

class CalendarLiveData() : MutableLiveData<MutableMap<LocalDate, MutableList<CalendarModel>>>() {
    private lateinit var query: Query
    private val calendarListener = CalendarListener()
    private var valueSize: Int = 0

    constructor(ref: DatabaseReference) : this() {
        query = ref
    }

    constructor(query: Query) : this() {
        this.query = query
    }

    override fun onActive() {
        super.onActive()

        query.addValueEventListener(calendarListener)
        Log.d(TAG, "onActive")
    }

    override fun onInactive() {
        super.onInactive()

        query.removeEventListener(calendarListener)
        Log.d(FriendLiveData.TAG, "onInactive")
    }

    companion object {
        const val TAG = "CalendarLiveData"
        const val AVATAR = "avatar"
        const val DATE = "date"
        const val EVENT_ID = "id"
        const val MEMBERS = "members"
        const val ADMIN = "admin"
        const val ADMIN_ID = "id"
        const val NAME = "name"
        const val PLACE = "place"
        const val PRICE = "price"
        const val TIME = "time"
    }

    fun pushDataToDB(date: LocalDate, event: CalendarModel) {
        if (!value!!.containsKey(date)) {
            // Кладём значение в liveData
            value!![date] = mutableListOf(event)
            // Записываем в БД по ссылке
            pushEventToBD(date, event, ++valueSize)
        } else {
            // Кладём значение в liveData
            val eventsThisDate = value!![date]
            eventsThisDate!!.add(event)
            value!![date] = eventsThisDate
            // Записываем в БД по ссылке
            pushEventToBD(date, event, ++valueSize)
        }
    }

    private fun pushEventToBD(date: LocalDate, event: CalendarModel, eventNumber: Int) {
        query.ref.child(eventNumber.toString()).apply {
            child(AVATAR).setValue(event.avatar)
            child(DATE).setValue(date.toString())
            child(EVENT_ID).setValue(eventNumber.toString())
            child(MEMBERS)
                .child(ADMIN)
                .child(ADMIN_ID)
                .setValue(event.adminId)
            child(NAME).setValue(event.eventPlace.name)
            child(PLACE).setValue(event.eventPlace.place)
            child(PRICE).setValue(event.eventPlace.price)
            child(TIME).setValue(localDateTimeToTimeString(event.time))
        }
    }

    fun pushDeleteEventToDB(date: LocalDate, calendarModel: CalendarModel) {
        // TODO delete only possible if its admin of the event
        // TODO decide reindexing problem
        if (value!![date]!!.size == 1) {
            value!!.remove(date)
        } else {
            val todayEvents = value!![date]
            todayEvents!!.remove(calendarModel)
            value!![date] = todayEvents
        }
        // Предполагаю, что id == eventNumber
        query.ref.child(calendarModel.id).removeValue()
    }

    private fun localDateTimeToTimeString(localDateTime: LocalDateTime): String {
        return localDateTime.toString().substringAfter('T')
    }

    inner class CalendarListener : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val calendarEvents: MutableMap<LocalDate, MutableList<CalendarModel>> = mutableMapOf()

            valueSize = snapshot.children.count()
            Log.d("DBSize", valueSize.toString())
            for (i in snapshot.children) {
                // Get data
                retrieveData(i, calendarEvents)
            }
            value = calendarEvents
        }

        override fun onCancelled(error: DatabaseError) {
            Log.d(TAG, "Can't listen query $query", error.toException())
        }

        private fun stringDateToLocalDate(date: String): LocalDate {
            val dateInStringList: List<String?> = date.split(
                delimiters = charArrayOf('-'),
                limit = 3
            )
            Log.d("CheckNull", date)
            try {
                return LocalDate.of(
                    dateInStringList[0]!!.toInt(),
                    dateInStringList[1]!!.toInt(),
                    dateInStringList[2]!!.toInt()
                )
            } catch (e: Exception) {
                return LocalDate.of(
                    2020,
                    11,
                    1
                )
            }
        }

        private fun timeToLocalDateTime(date: LocalDate, time: String): LocalDateTime {
            val timeInStringList = time.split(
                delimiters = charArrayOf(':'),
                limit = 2
            )
            try {
                return date.atTime(
                    timeInStringList[0].toInt(),
                    timeInStringList[1].toInt()
                )
            } catch (e: Exception) {
                return date.atTime(
                    0,
                    0
                )
            }
        }

        private fun retrieveData(
            i: DataSnapshot,
            calendarEvents: MutableMap<LocalDate, MutableList<CalendarModel>>
        ) {
            val localDate = stringDateToLocalDate(i.child("date").value.toString())
            val localDateTime = timeToLocalDateTime(
                localDate,
                i.child("time").value.toString()
            )
            val mAuth = FirebaseAuth.getInstance().currentUser.toString()
            val calendarEvent = CalendarModel(
                time = localDateTime,
                CalendarModel.Place(
                    name = i.child("name").value.toString(),
                    price = i.child("price").value.toString(),
                    place = i.child("place").value.toString(),
                ),
                color = R.color.teal_700,
                id = i.child("id").value.toString(),
                adminId = i.child("members")
                    .child("admin")
                    .child("id").value.toString()
            )
            if (!calendarEvents.containsKey(localDate)) {
                calendarEvents.put(localDate, mutableListOf(calendarEvent))
            } else {
                val eventsThisDate = calendarEvents[localDate]
                eventsThisDate!!.add(calendarEvent)
                calendarEvents.put(localDate, eventsThisDate)
            }
        }
    }
}
