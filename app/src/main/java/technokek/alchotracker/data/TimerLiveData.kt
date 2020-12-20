package technokek.alchotracker.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import technokek.alchotracker.data.models.TimerModel

class TimerLiveData() : MutableLiveData<MutableList<TimerModel>>() {

    private lateinit var query: Query
    private val timersListener = TimerListener()

    constructor(ref: DatabaseReference) : this() {
        query = ref
    }

    constructor(query: Query) : this() {
        this.query = query
    }

    override fun onActive() {
        super.onActive()

        query.addValueEventListener(timersListener)
        Log.d(TAG, "onActive")
    }

    override fun onInactive() {
        super.onInactive()

        query.removeEventListener(timersListener)
        Log.d(TAG, "onInactive")
    }

    companion object {
        const val TAG = "TimerLiveData"
    }

    inner class TimerListener : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val timers: MutableList<TimerModel> = mutableListOf()

            for (i in snapshot.children) {
                retrieveData(i, timers)
            }
            value = timers
            Log.d("TimersDataChange", timers.toString())
        }

        private fun retrieveData(i: DataSnapshot, timers: MutableList<TimerModel>) {
            val localDate = CalendarLiveData.stringDateToLocalDate(i.child("date").value.toString())
            val localDateTime = CalendarLiveData.timeToLocalDateTime(
                localDate,
                i.child("time").value.toString()
            )
            val time = TimerModel(
                timeStart = localDateTime
            )
            timers.add(time)
        }

        override fun onCancelled(error: DatabaseError) {
            Log.d(TAG, "Can't listen query $query", error.toException())
        }
    }
}