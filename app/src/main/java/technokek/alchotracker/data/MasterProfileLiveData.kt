package technokek.alchotracker.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import technokek.alchotracker.data.models.MasterProfileModel

class MasterProfileLiveData() : MutableLiveData<MasterProfileModel>() {
    private lateinit var query: Query
    private lateinit var mAuth: FirebaseAuth
    private val profileListener = ProfileListener()

    constructor(query: Query, mAuth: FirebaseAuth) : this() {
        this.query = query
        this.mAuth = mAuth
    }

    override fun onActive() {
        super.onActive()

        query.addValueEventListener(profileListener)
        Log.d(TAG, "onActive")
    }

    override fun onInactive() {
        super.onInactive()

        query.removeEventListener(profileListener)
        Log.d(TAG, "onInactive")
    }

    inner class ProfileListener : ValueEventListener {

        override fun onDataChange(snapshot: DataSnapshot) {
            getProfile(snapshot.child(mAuth.currentUser?.uid.toString()))
        }

        override fun onCancelled(error: DatabaseError) {
            Log.d("ERROR_MSG", error.toString())
        }
    }

    fun getProfile(x: DataSnapshot) {
        value = MasterProfileModel(
            x.child("avatar").value.toString(),
            x.child("name").value.toString(),
            x.child("status").value.toString(),
            x.child("alchoinfo").child("friendsCount").getValue(Int::class.java)!!,
            x.child("alchoinfo").child("eventsCount").getValue(Int::class.java)!!,
            x.child("alchoinfo").child("favouriteDrink").value.toString()
        )
    }

    companion object {
        const val TAG = "MasterProfileLiveData"
    }
}
