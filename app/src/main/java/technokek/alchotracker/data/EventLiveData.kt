package technokek.alchotracker.data

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import technokek.alchotracker.data.models.EventModel
import technokek.alchotracker.network.ApiRepo

class EventLiveData() : MutableLiveData<MutableList<EventModel>>() {

    private lateinit var query: Query
    private val eventListener = EventListener()

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
                ApiRepo.child("${i.key}.jpg").downloadUrl.addOnSuccessListener {
                    val event = EventModel(
                        i.child("id").getValue(Int::class.java)!!,
                        i.child("name").value.toString(),
                        it.toString()
                    )
                    events.add(event)
                }
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.d(TAG, "Can't listen query $query", error.toException())
        }
    }
}