package technokek.alchotracker.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import technokek.alchotracker.data.models.FriendModel

class FriendRequestLiveData() : MutableLiveData<MutableList<FriendModel>>() {

    private lateinit var query: Query
    private val friendRequestListener = FriendRequestListener()
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

        query.addValueEventListener(friendRequestListener)
        Log.d(TAG, "onActive")
    }

    override fun onInactive() {
        super.onInactive()

        query.removeEventListener(friendRequestListener)
        Log.d(TAG, "onInactive")
    }

    fun acceptRequest(uid: String, pos: Int, currUser: FriendModel) {
        var valueToPush = value!![pos]
        var outgoing = valueToPush.outgoing
        var outFriendsCount = valueToPush.friendsCount
        var outFriendList = valueToPush.friendsList

        outgoing.replace(currentUser!!.uid, "")
        if (outgoing.isEmpty()) {
            outgoing = ""
        }
        outFriendsCount += 1
        if (outFriendList.isEmpty()) {
            outFriendList = currentUser.uid
        } else {
            outFriendList.plus(";").plus(currentUser.uid)
        }

        valueToPush = currUser

        var incoming = valueToPush.outgoing
        var inFriendCount = valueToPush.friendsCount + 1
        var inFriendList = valueToPush.friendsList

        Log.d("Test", "$inFriendList !!!!")

        incoming.replace(uid, "")
        if (incoming.isEmpty()) {
            incoming = ""
        }
        inFriendList = if (inFriendList.isEmpty()) {
            uid
        } else {
            inFriendList.plus(";").plus(uid)
        }

        Log.d("Test", "$inFriendList Plus")

        query.ref.child(uid)
            .child("friends")
            .child("requests")
            .child("outgoing")
            .setValue(outgoing)
        query.ref.child(uid)
            .child("alchoinfo")
            .child("friendsCount")
            .setValue(outFriendsCount)
        query.ref.child(uid)
            .child("friends")
            .child("list").setValue(outFriendList)

        query.ref.child(currentUser.uid)
            .child("friends")
            .child("requests")
            .child("incoming")
            .setValue(incoming)
        query.ref.child(currentUser.uid)
            .child("alchoinfo")
            .child("friendsCount")
            .setValue(inFriendCount)
        query.ref.child(currentUser.uid)
            .child("friends")
            .child("list")
            .setValue(inFriendList)
    }

    fun denyRequest(uid: String, pos: Int, currUser: FriendModel) {
        var valueToPush = value!![pos]
        val outgoing = valueToPush.outgoing

        outgoing.replace(currentUser!!.uid, "")

        valueToPush = currUser

        val incoming = valueToPush.outgoing

        incoming.replace(uid, "")

        query.ref.child(uid)
            .child("friends")
            .child("requests")
            .child("outgoing")
            .setValue(outgoing + "")

        query.ref.child(currentUser.uid)
            .child("friends")
            .child("requests")
            .child("incoming")
            .setValue(incoming + "")
    }

    companion object {
        const val TAG = "FriendRequestLiveData"
    }

    inner class FriendRequestListener : ValueEventListener {

        override fun onDataChange(snapshot: DataSnapshot) {
            val friendRequests: MutableList<FriendModel> = mutableListOf()
            val listRequestFriend: MutableList<String> = getListRequestFriends(snapshot)

            for (i in snapshot.children) {
                if (listRequestFriend.contains(i.key)) {
                    val potentialFriend = FriendModel(
                        id = i.key.toString(),
                        name = i.child("name").value.toString(),
                        avatar = i.child("avatar").value.toString(),
                        incoming = i.child("friends").child("incoming").value.toString(),
                        outgoing = i.child("friends").child("outgoing").value.toString(),
                        friendsCount = i.child("alchoinfo").child("friendsCount")
                            .getValue(Int::class.java)!!,
                        friendsList = i.child("friends").child("list").value.toString()
                    )
                    friendRequests.add(potentialFriend)
                }
            }

            value = friendRequests
        }

        override fun onCancelled(error: DatabaseError) {
            Log.d(TAG, "Can't listen query $query", error.toException())
        }

        private fun getListRequestFriends(snapshot: DataSnapshot): MutableList<String> {
            val user = snapshot.child(currentUser!!.uid)
            val rawList = user.child("friends").child("requests").child("incoming").value.toString()

            Log.d("test", "$user : ${rawList.split(";").toMutableList()}")

            return rawList.split(";").toMutableList()
        }
    }
}
