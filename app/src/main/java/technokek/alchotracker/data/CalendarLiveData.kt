package technokek.alchotracker.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import technokek.alchotracker.data.models.CalendarModel
import technokek.alchotracker.data.models.FriendModel
import java.time.LocalDate
import java.time.LocalDateTime
import technokek.alchotracker.R

class CalendarLiveData() : MutableLiveData<MutableMap<LocalDate, MutableList<CalendarModel>>>() {
    private lateinit var query: Query
    private val calendarListener = CalendarListener()

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
    }

    /*fun refresh() {
        query.
    }*/

    inner class CalendarListener : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val calendarEvents: MutableMap<LocalDate, MutableList<CalendarModel>> = mutableMapOf()


            for (i in snapshot.children) {
                //Get data
                retrieveData(i, calendarEvents)
            }
            value = calendarEvents
        }

        override fun onCancelled(error: DatabaseError) {
            Log.d(TAG, "Can't listen query $query", error.toException())
        }

        private fun stringDateToLocalDate(date: String): LocalDate {
            val dateInStringList = date.split(
                delimiters = charArrayOf('-'),
                limit = 3
            )
            return LocalDate.of(
                dateInStringList[0].toInt(),
                dateInStringList[1].toInt(),
                dateInStringList[2].toInt()
            )
        }

        private fun timeToLocalDateTime(date: LocalDate, time: String): LocalDateTime {
            val timeInStringList = time.split(
                delimiters = charArrayOf(':'),
                limit = 2
            )
            return date.atTime(
                timeInStringList[0].toInt(),
                timeInStringList[1].toInt()
            )
        }

        private fun retrieveData(i:DataSnapshot,
                                 calendarEvents: MutableMap<LocalDate, MutableList<CalendarModel>>) {
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
            }
            else {
                val eventsThisDate = calendarEvents[localDate]
                eventsThisDate!!.add(calendarEvent)
                calendarEvents.put(localDate, eventsThisDate)
            }
        }
    }
}
