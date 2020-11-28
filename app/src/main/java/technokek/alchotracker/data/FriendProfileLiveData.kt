package technokek.alchotracker.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import technokek.alchotracker.data.models.FriendProfileModel

class FriendProfileLiveData(): MutableLiveData<FriendProfileModel>() {
    private lateinit var uid: String
    private lateinit var query: Query
    private val profileListener = ProfileListener()

    constructor(query: Query, uid: String) : this() {
        this.query = query
        this.uid = uid
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
            setProfile(snapshot.child(uid))
        }

        override fun onCancelled(error: DatabaseError) {
            Log.d("kek", error.toString())
        }

    }

    fun setProfile(x: DataSnapshot) {
        value = FriendProfileModel(
            x.child("avatar").value.toString(),
            x.child("name").value.toString(),
            x.child("status").value.toString(),
            x.child("info").child("friendsCount").getValue(Int::class.java)!!,
            x.child("info").child("eventsCount").getValue(Int::class.java)!!,
            x.child("info").child("favouriteDrink").value.toString()
        )
    }

    companion object {
        const val TAG = "FriendProfileLiveData"
    }
}