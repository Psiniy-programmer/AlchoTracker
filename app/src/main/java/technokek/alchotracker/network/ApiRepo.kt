package technokek.alchotracker.network

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import technokek.alchotracker.data.models.EventModel

class ApiRepo {

    private val reference: DatabaseReference = FirebaseDatabase.getInstance().reference
    private lateinit var events: ArrayList<EventModel>

    fun loadData() {
        var query = reference.child("events")
        query.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in snapshot.children) {
                    events.add(i.getValue(EventModel::class.java)!!)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun getEvent() : MutableLiveData<ArrayList<EventModel>> {
        val list = MutableLiveData<ArrayList<EventModel>>()
        list.value = events
        return list
    }
}
