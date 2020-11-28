package technokek.alchotracker.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import technokek.alchotracker.data.models.FriendModel

class FriendLiveData() : MutableLiveData<MutableList<FriendModel>>() {

    private lateinit var query: Query
    private val friendListener = FriendListener()

    constructor(ref: DatabaseReference) : this() {
        query = ref
    }

    constructor(query: Query) : this() {
        this.query = query
    }

    override fun onActive() {
        super.onActive()

        query.addValueEventListener(friendListener)
        Log.d(TAG, "onActive")
    }

    override fun onInactive() {
        super.onInactive()

        query.removeEventListener(friendListener)
        Log.d(TAG, "onInactive")
    }

    companion object {
        const val TAG = "FriendLiveData"
    }

    inner class FriendListener : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val friends: MutableList<FriendModel> = mutableListOf()

            for (i in snapshot.children) {
                val friend = FriendModel(
                    i.key.toString(),
                    i.child("name").value.toString(),
                    i.child("avatar").value.toString()
                )
                friends.add(friend)
            }

            value = friends
        }

        override fun onCancelled(error: DatabaseError) {
            Log.d(TAG, "Can't listen query $query", error.toException())
        }
    }
}
