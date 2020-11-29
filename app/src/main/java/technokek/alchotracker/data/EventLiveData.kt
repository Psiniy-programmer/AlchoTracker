package technokek.alchotracker.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import technokek.alchotracker.data.models.EventModel

class EventLiveData() : MutableLiveData<MutableList<EventModel>>() {

    private lateinit var query: Query
    private val eventListener = EventListener()

    constructor(ref: DatabaseReference) : this() {
        query = ref
    }

    constructor(query: Query) : this() {
        this.query = query
    }

    override fun onActive() {
        super.onActive()

        query.addValueEventListener(eventListener)
        Log.d(TAG, "onActive")
    }

    override fun onInactive() {
        super.onInactive()

        query.removeEventListener(eventListener)
        Log.d(TAG, "onInactive")
    }

    companion object {
        const val TAG = "EventLiveData"
    }

    inner class EventListener : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val events: MutableList<EventModel> = mutableListOf()

            for (i in snapshot.children) {
                val event = EventModel(
                    i.child("id").getValue(Int::class.java)!!,
                    i.child("name").value.toString(),
                    i.child("avatar").value.toString()
                )
                events.add(event)
            }

            value = events
        }

        override fun onCancelled(error: DatabaseError) {
            Log.d(TAG, "Can't listen query $query", error.toException())
        }
    }
}