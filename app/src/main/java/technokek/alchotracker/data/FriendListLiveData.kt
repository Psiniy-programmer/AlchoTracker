package technokek.alchotracker.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import technokek.alchotracker.data.models.FriendModel

class FriendListLiveData() : MutableLiveData<MutableList<FriendModel>>() {

    private lateinit var query: Query
    private lateinit var uidFriend: String
    private val friendListListener = FriendListListener()

    constructor(query: Query, uid: String) : this() {
        this.query = query
        this.uidFriend = uid
    }

    constructor(ref: DatabaseReference, uid: String) : this() {
        this.query = ref
        this.uidFriend = uid
    }

    override fun onActive() {
        super.onActive()

        query.addValueEventListener(friendListListener)
        Log.d(TAG, "onActive")
    }

    override fun onInactive() {
        super.onInactive()

        query.removeEventListener(friendListListener)
        Log.d(TAG, "onInactive")
    }

    companion object {
        const val TAG = "FriendLiveData"
    }

    inner class FriendListListener : ValueEventListener {
        private val mAuth = FirebaseAuth.getInstance()
        private val currentUser = mAuth.currentUser

        override fun onDataChange(snapshot: DataSnapshot) {
            val friends: MutableList<FriendModel> = mutableListOf()
            val listFriend: MutableList<String> = getListFriends(snapshot)

            for (i in snapshot.children) {
                if (listFriend.contains(i.key)) {
                    val friend = FriendModel(
                        id = i.key.toString(),
                        name = i.child(Constants.NAME).value.toString(),
                        avatar = i.child(Constants.AVATAR).value.toString(),
                        incoming = i.child(Constants.FRIENDS).child(Constants.INCOMING_REQUESTS).value.toString(),
                        outgoing = i.child(Constants.FRIENDS).child(Constants.OUTGOING_REQUESTS).value.toString(),
                        friendsCount = i.child(Constants.ALCHOINFO).child(Constants.FRIENDSCOUNT)
                            .getValue(Int::class.java)!!,
                        friendsList = i.child(Constants.FRIENDS).child(Constants.LIST).value.toString()
                    )
                    friends.add(friend)
                }
            }

            value = friends
        }

        override fun onCancelled(error: DatabaseError) {
            Log.d(TAG, "Can't listen query $query", error.toException())
        }

        private fun getListFriends(snapshot: DataSnapshot): MutableList<String> {
            val user = snapshot.child(uidFriend)
            val rawList = user.child("friends").child("list").value.toString()

            return rawList.split(";").toMutableList()
        }
    }
}
