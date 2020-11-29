package technokek.alchotracker.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import technokek.alchotracker.data.models.FriendModel

class FriendRequestLiveData() : MutableLiveData<MutableList<FriendModel>>() {

    private lateinit var query: Query
    private val friendRequestListener = FriendRequestListener()

    constructor(ref: DatabaseReference) : this() {
        query = ref
    }

    constructor(query: Query) : this() {
        this.query = query
    }

    override fun onActive() {
        super.onActive()

        query.addValueEventListener(friendRequestListener)
        Log.d(TAG, "onActive")
    }

    override fun onInactive() {
        super.onInactive()

        query.removeEventListener(friendRequestListener)
        Log.d(TAG, "onInactive")
    }

    fun acceptRequest(uid: String) {
        val s: String = query.ref.child(uid)
            .child("friends")
            .child("requests")
            .child("outgoing").

        Log.d("test1", "${s}")
    }

    fun denyRequest(uid: String) {

    }

    companion object {
        const val TAG = "FriendRequestLiveData"
    }

    inner class FriendRequestListener : ValueEventListener {
        private val mAuth = FirebaseAuth.getInstance()
        private val currentUser = mAuth.currentUser

        override fun onDataChange(snapshot: DataSnapshot) {
            val friendRequests: MutableList<FriendModel> = mutableListOf()
            val listRequestFriend: MutableList<String> = getListRequestFriends(snapshot)

            for (i in snapshot.children) {
                if (listRequestFriend.contains(i.key)) {
                    val potentialFriend = FriendModel(
                        i.key.toString(),
                        i.child("name").value.toString(),
                        i.child("avatar").value.toString()
                    )
                    friendRequests.add(potentialFriend)
                }
            }

            value = friendRequests
        }

        override fun onCancelled(error: DatabaseError) {
            Log.d(TAG, "Can't listen query $query", error.toException())
        }

        private fun getListRequestFriends(snapshot: DataSnapshot) : MutableList<String> {
            val user = snapshot.child(currentUser!!.uid)
            val rawList = user.child("friends").child("requests").child("incoming").value.toString()

            Log.d("test", "$user : ${rawList.split(";").toMutableList()}")

            return rawList.split(";").toMutableList()
        }
    }
}
