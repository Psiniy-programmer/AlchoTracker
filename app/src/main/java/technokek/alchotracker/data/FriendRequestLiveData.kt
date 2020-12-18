package technokek.alchotracker.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import technokek.alchotracker.data.models.FriendModel
import technokek.alchotracker.data.Constants.*

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
        val outgoing = friend.outgoing.split(";").filter { it != currentUser!!.uid }
        var outFriendsCount = friend.friendsCount
        var outFriendList = friend.friendsList

        Log.d("Test", "outgoing: $outgoing")
        var outgoingChange = String()
        for (i in outgoing) {
            outgoingChange = if (outgoingChange.isEmpty()) {
                i
            } else {
                outgoingChange.plus(";").plus(i)
            }
        }
        outFriendsCount += 1
        outFriendList = if (outFriendList.isEmpty()) {
            currentUser!!.uid
        } else {
            "${outFriendList};${currentUser!!.uid}"
        }

        if (outgoingChange.isEmpty()) {
            query.ref.child(uid)
                .child(FRIENDS)
                .child(REQUESTS)
                .child(OUTGOING)
                .setValue("")
        } else {
            query.ref.child(uid)
                .child(FRIENDS)
                .child(REQUESTS)
                .child(OUTGOING)
                .setValue(outgoingChange)
        }
        query.ref.child(uid)
            .child(ALCHOINFO)
            .child(FRIENDSCOUNT)
            .setValue(outFriendsCount)
        query.ref.child(uid)
            .child(FRIENDS)
            .child(LIST).setValue(outFriendList)
    }

    private fun pushAcceptToCurrUser(user: FriendModel, uid: String) {
        val incoming = user.incoming.split(";").filter { it != uid }
        val inFriendCount = user.friendsCount + 1
        var inFriendList = user.friendsList
        val currentID = user.id

        Log.d("Test", "incoming: $incoming")

        var incomingChange = String()
        for (i in incoming) {
            incomingChange = if (incomingChange.isEmpty()) {
                i
            } else {
                incomingChange.plus(";").plus(i)
            }
        }
        Log.d("Test", "incomingChange: $incomingChange")
        inFriendList = if (inFriendList.isEmpty()) {
            uid
        } else {
            inFriendList.plus(";").plus(uid)
        }

        if (incomingChange.isEmpty()) {
            query.ref.child(currentID)
                .child(FRIENDS)
                .child(REQUESTS)
                .child(INCOMING)
                .setValue("")
        } else {
            query.ref.child(currentID)
                .child(FRIENDS)
                .child(REQUESTS)
                .child(INCOMING)
                .setValue(incomingChange)
        }
        query.ref.child(currentID)
            .child(ALCHOINFO)
            .child(FRIENDSCOUNT)
            .setValue(inFriendCount)
        query.ref.child(currentID)
            .child(FRIENDS)
            .child(LIST)
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

        val outgoing = valueToPush.outgoing.split(";").filter { it != currentUser!!.uid }
        var outgoingChange = String()
        for (i in outgoing) {
            outgoingChange = if (outgoingChange.isEmpty()) {
                i
            } else {
                outgoingChange.plus(";").plus(i)
            }
        }

        valueToPush = currUser

        val incoming = valueToPush.incoming.split(";").filter { it != uid }
        var incomingChange = String()
        for (i in incoming) {
            incomingChange = if (incomingChange.isEmpty()) {
                i
            } else {
                incomingChange.plus(";").plus(i)
            }
        }

        if (outgoingChange.isEmpty()) {
            query.ref.child(uid)
                .child(FRIENDS)
                .child(REQUESTS)
                .child(OUTGOING)
                .setValue("")
        } else {
            query.ref.child(uid)
                .child(FRIENDS)
                .child(REQUESTS)
                .child(OUTGOING)
                .setValue(outgoingChange)
        }

        if (incomingChange.isEmpty()) {
            query.ref.child(currentUser!!.uid)
                .child(FRIENDS)
                .child(REQUESTS)
                .child(INCOMING)
                .setValue("")
        } else {
            query.ref.child(currentUser!!.uid)
                .child(FRIENDS)
                .child(REQUESTS)
                .child(INCOMING)
                .setValue(incomingChange)
        }
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
                        name = i.child(NAME).value.toString(),
                        avatar = i.child(AVATAR).value.toString(),
                        incoming = i.child(FRIENDS).child(REQUESTS)
                            .child(INCOMING).value.toString(),
                        outgoing = i.child(FRIENDS).child(REQUESTS)
                            .child(OUTGOING).value.toString(),
                        friendsCount = i.child(ALCHOINFO).child(FRIENDSCOUNT)
                            .getValue(Int::class.java)!!,
                        friendsList = i.child(FRIENDS).child(LIST).value.toString()
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
            val rawList = user.child(FRIENDS).child(REQUESTS).child(INCOMING).value.toString()

            Log.d("test", "$user : ${rawList.split(";").toMutableList()}")

            return rawList.split(";").toMutableList()
        }
    }
}
