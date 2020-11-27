package technokek.alchotracker.network

import android.util.Log
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import technokek.alchotracker.data.models.EventModel

object ApiRepo {

    private val reference: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val storageRef: StorageReference = FirebaseStorage.getInstance().reference

    fun getEvent(): MutableList<EventModel> {
        val events: MutableList<EventModel> = mutableListOf()
        var query = reference.child("events")

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in snapshot.children) {
                    storageRef.child("${i.key}.jpg").downloadUrl.addOnSuccessListener {
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
                Log.d("ApiFail", "Failed to read value.", error.toException())
            }
        })

        return events
    }
}
