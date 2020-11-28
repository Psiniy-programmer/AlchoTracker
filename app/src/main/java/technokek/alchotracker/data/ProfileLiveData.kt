package technokek.alchotracker.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import technokek.alchotracker.data.models.ProfileModel

class ProfileLiveData() : MutableLiveData<ProfileModel>() {
    private lateinit var query: Query
    private val profileListener = ProfileListener()
    
    constructor(query: Query) : this() {
        this.query = query
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

    inner class ProfileListener: ValueEventListener {

        override fun onDataChange(snapshot: DataSnapshot) {
            setProfile(snapshot.child("1"))
        }

        override fun onCancelled(error: DatabaseError) {
            Log.d("kek", error.toString())
        }

    }

     fun setProfile(x: DataSnapshot) {
         value = ProfileModel("1",
             x.child("name").value.toString(),
             x.child("status").value.toString(),
             x.child("info").child("friendsCount").getValue(Int::class.java)!!,
             x.child("info").child("eventsCount").getValue(Int::class.java)!!,
             x.child("info").child("favouriteDrink").value.toString()
         )
    }

    companion object {
        const val TAG = "ProfileLiveData"
    }
}