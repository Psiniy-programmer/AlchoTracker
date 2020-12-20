package technokek.alchotracker.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import technokek.alchotracker.data.models.FriendModel
import technokek.alchotracker.data.Constants.*

class CurrentUserLiveData() : MutableLiveData<FriendModel>() {

    private lateinit var query: Query
    private val mCurrentUserListener = CurrentUserListener()
    private val mAuth = FirebaseAuth.getInstance()
    private val currentUser = mAuth.currentUser

    constructor(ref: DatabaseReference) : this() {
        query = ref
    }

    constructor(query: Query) : this() {
        this.query = query
    }

    override fun onActive() {
        super.onActive()

        query.addValueEventListener(mCurrentUserListener)
        Log.d(TAG, "onActive")
    }

    override fun onInactive() {
        super.onInactive()

        query.removeEventListener(mCurrentUserListener)
        Log.d(TAG, "onInactive")
    }

    companion object {
        const val TAG = "CurrentUserLiveData"
    }

    inner class CurrentUserListener : ValueEventListener {

        override fun onDataChange(snapshot: DataSnapshot) {
            var currUser = FriendModel()

            for (i in snapshot.children) {
                if (currentUser!!.uid == i.key) {
                    currUser = FriendModel(
                        id = i.key.toString(),
                        name = i.child(NAME).value.toString(),
                        avatar = i.child(AVATAR).value.toString(),
                        incoming = i.child(FRIENDS).child(REQUESTS)
                            .child(INCOMING_REQUESTS).value.toString(),
                        outgoing = i.child(FRIENDS).child(REQUESTS)
                            .child(OUTGOING_REQUESTS).value.toString(),
                        friendsCount = i.child(ALCHOINFO).child(FRIENDSCOUNT)
                            .getValue(Int::class.java)!!,
                        friendsList = i.child(FRIENDS).child(LIST).value.toString()
                    )
                    break
                }
            }

            Log.d("GetCurrentuser", currUser.toString())

            value = currUser
        }

        override fun onCancelled(error: DatabaseError) {
            Log.d(TAG, "Can't listen query $query", error.toException())
        }
    }
}
