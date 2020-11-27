package technokek.alchotracker.network

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import technokek.alchotracker.data.models.EventModel

class ApiRepo {

    private val reference: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val events: ArrayList<EventModel> = ArrayList()

    fun loadData() {
        var query = reference.child("events")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in snapshot.children) {
                    val event = EventModel(
                        i.child("id").getValue(Int::class.java)!!,
                        i.child("name").value.toString(),
                        i.child("avatar").value.toString()
                    )
                    events.add(event)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun getEvent(): MutableLiveData<ArrayList<EventModel>> {
        val list = MutableLiveData<ArrayList<EventModel>>()
        loadData()
        list.value = events

        return list
    }
}
