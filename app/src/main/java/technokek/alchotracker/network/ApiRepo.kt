package technokek.alchotracker.network

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import technokek.alchotracker.data.models.EventModel
import technokek.alchotracker.data.models.FriendModel

class ApiRepo {

    private val reference: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val storageRef: StorageReference = FirebaseStorage.getInstance().reference
    private val events: ArrayList<EventModel> = ArrayList()
    private val friends: ArrayList<FriendModel> = ArrayList()

    fun getFriend(): MutableLiveData<ArrayList<FriendModel>> {
        var query = reference.child("users")
        query.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in snapshot.children) {
                    storageRef.child("${i.key}.jpg").downloadUrl.addOnSuccessListener {
                        val event = FriendModel(
                            i.key!!.toInt(),
                            i.child("name").value.toString(),
                            it.toString()
                        )
                        friends.add(event)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        val list = MutableLiveData<ArrayList<FriendModel>>()
        list.value = friends

        return list
    }

    fun getEvent(): MutableLiveData<ArrayList<EventModel>> {
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
                TODO("Not yet implemented")
            }
        })

        val list = MutableLiveData<ArrayList<EventModel>>()
        list.value = events

        return list
    }
}
