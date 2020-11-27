package technokek.alchotracker.data

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import technokek.alchotracker.data.models.ProfileModel

class ProfileLiveData : LiveData<ProfileModel> {
    private val database = FirebaseDatabase.getInstance()
    private val dbRef = database.getReference("users")
    private val storage = FirebaseStorage.getInstance()
    private val sRef = storage.reference

    constructor() {
//        TODO("Переделать на запрос данных из зареганных юзеров")
        value = ProfileModel("Test name", "status", 232, 222, "default")
    }

    constructor(uid: String) {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (x in snapshot.children) {
                    if (x.key == uid) {
                        sRef.child("${uid}.jpg").downloadUrl.addOnSuccessListener {
                            value = ProfileModel(
                                x.child("name").value.toString(),
                                x.child("status").value.toString(),
                                x.child("info").child("friendsCount").getValue(Int::class.java)!!,
                                x.child("info").child("eventsCount").getValue(Int::class.java)!!,
                                it.toString()
                            )
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
            }
        })
    }
}