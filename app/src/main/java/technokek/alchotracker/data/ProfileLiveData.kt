package technokek.alchotracker.data

import androidx.lifecycle.LiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import technokek.alchotracker.data.models.ProfileModel

class ProfileLiveData : LiveData<ProfileModel> {
    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("users")

    constructor() {
//        TODO("Переделать на запрос данных из зареганных юзеров")
        value = ProfileModel("Test name", "status", 232, 222)
        for (i in 0..100) {
            value!!.preferencesList.add("Test drink ${i.toString()}")
        }
    }

    constructor(uid: String) {
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (x in snapshot.children) {
                    if (x.key == uid) {
                            value = ProfileModel(
                            x.child("name").value.toString(),
                            x.child("status").value.toString(),
                            x.child("info").child("friendsCount").getValue(Int::class.java)!!,
                            x.child("info").child("eventsCount").getValue(Int::class.java)!!
                        )
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
            }
        })
    }
}