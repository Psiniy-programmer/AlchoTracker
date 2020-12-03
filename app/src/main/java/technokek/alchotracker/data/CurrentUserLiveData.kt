package technokek.alchotracker.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import technokek.alchotracker.data.models.FriendModel

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
                        name = i.child("name").value.toString(),
                        avatar = i.child("avatar").value.toString(),
                        incoming = i.child("friends").child("requests")
                            .child("incoming").value.toString(),
                        outgoing = i.child("friends").child("requests")
                            .child("outgoing").value.toString(),
                        friendsCount = i.child("alchoinfo").child("friendsCount")
                            .getValue(Int::class.java)!!,
                        friendsList = i.child("friends").child("list").value.toString()
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
