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

    private fun pushAcceptToFriend(friend: FriendModel, uid: String) {
        var outgoing = friend.outgoing.split(";")
        var outFriendsCount = friend.friendsCount
        var outFriendList = friend.friendsList

        Log.d("Test", "outgoing: $outgoing")
        var outgoingChange = String()
        for (i in outgoing) {
            if (i != currentUser!!.uid) {
                if (outgoingChange.isEmpty()) {
                    outgoingChange = i
                } else {
                    outgoingChange.plus(";").plus(i)
                }
            }
        }
        Log.d("Test", "outgoingChange: $outgoingChange")
        outFriendsCount += 1
        if (outFriendList.isEmpty()) {
            outFriendList = currentUser!!.uid
        } else {
            outFriendList.plus(";").plus(currentUser!!.uid)
        }

        query.ref.child(uid)
            .child("friends")
            .child("requests")
            .child("outgoing")
            .setValue(outgoingChange)
        query.ref.child(uid)
            .child("alchoinfo")
            .child("friendsCount")
            .setValue(outFriendsCount)
        query.ref.child(uid)
            .child("friends")
            .child("list").setValue(outFriendList)
    }

    private fun pushAcceptToCurrUser(user: FriendModel, uid: String) {
        var incoming = user.incoming.split(";")
        var inFriendCount = user.friendsCount + 1
        var inFriendList = user.friendsList
        var currentID = user.id

        Log.d("Test", "incoming: $incoming")

        var incomingChange = String()
        for (i in incoming) {
            if (i != uid) {
                if (incomingChange.isEmpty()) {
                    incomingChange = i
                } else {
                    incomingChange.plus(";").plus(i)
                }
            }
        }
        Log.d("Test", "incomingChange: $incomingChange")
        inFriendList = if (inFriendList.isEmpty()) {
            uid
        } else {
            inFriendList.plus(";").plus(uid)
        }

        query.ref.child(currentID)
            .child("friends")
            .child("requests")
            .child("incoming")
            .setValue(incomingChange)
        query.ref.child(currentID)
            .child("alchoinfo")
            .child("friendsCount")
            .setValue(inFriendCount)
        query.ref.child(currentID)
            .child("friends")
            .child("list")
            .setValue(inFriendList)
    }

    fun acceptRequest(uid: String, pos: Int, currUser: FriendModel) {
        var valueToPush = value!![pos]
        pushAcceptToFriend(valueToPush, uid)

        valueToPush = currUser
        pushAcceptToCurrUser(valueToPush, uid)
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
                        incoming = i.child("friends").child("requests")
                            .child("incoming").value.toString(),
                        outgoing = i.child("friends").child("requests")
                            .child("outgoing").value.toString(),
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
