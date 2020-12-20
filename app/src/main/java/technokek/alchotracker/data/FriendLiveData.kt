package technokek.alchotracker.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import technokek.alchotracker.data.models.FriendModel
import technokek.alchotracker.data.Constants.*

class FriendLiveData() : MutableLiveData<MutableList<FriendModel>>() {

    private lateinit var query: Query
    private val friendListener = FriendListener()
    private var boolean = false

    constructor(ref: DatabaseReference) : this() {
        query = ref
    }

    constructor(ref: DatabaseReference, boolean: Boolean) : this() {
        this.query = ref
        this.boolean = boolean
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
        private val mAuth = FirebaseAuth.getInstance()
        private val currentUser = mAuth.currentUser

        override fun onDataChange(snapshot: DataSnapshot) {
            val friends: MutableList<FriendModel> = mutableListOf()
            val listFriend: MutableList<String> = getListFriends(snapshot)

            if (!boolean) {
                for (i in snapshot.children) {
                    if (listFriend.contains(i.key)) {
                        val friend = FriendModel(
                            id = i.key.toString(),
                            name = i.child(NAME).value.toString(),
                            avatar = i.child(AVATAR).value.toString(),
                            incoming = i.child(FRIENDS).child(INCOMING_REQUESTS).value.toString(),
                            outgoing = i.child(FRIENDS).child(OUTGOING_REQUESTS).value.toString(),
                            friendsCount = i.child(ALCHOINFO).child(FRIENDSCOUNT)
                                .getValue(Int::class.java)!!,
                            friendsList = i.child(FRIENDS).child(LIST).value.toString()
                        )
                        friends.add(friend)
                    }
                }
            } else {
                val listChatID: MutableList<String> = mutableListOf()
                val snap = snapshot
                    .child(currentUser!!.uid)
                    .child(Constants.CHATID)
                for (i in snap.children) {
                    listChatID.add(i.key.toString())
                }

                Log.d("SUKA", listChatID.toString())
                var chatID = ""

                for (i in snapshot.children) {
                    var bool = false
                    if (listFriend.contains(i.key)) {
                        for (j in listChatID) {
                            if (j.contains(i.key.toString())) {
                                bool = true
                                chatID = j
                                break
                            }

                            Log.d("SUKA", j)
                        }

                        if (bool) {
                            val friend = FriendModel(
                                id = i.key.toString(),
                                name = i.child(NAME).value.toString(),
                                avatar = i.child(AVATAR).value.toString(),
                                incoming = i.child(FRIENDS).child(INCOMING_REQUESTS).value.toString(),
                                outgoing = i.child(FRIENDS).child(OUTGOING_REQUESTS).value.toString(),
                                friendsCount = i.child(ALCHOINFO).child(FRIENDSCOUNT)
                                    .getValue(Int::class.java)!!,
                                friendsList = i.child(FRIENDS).child(LIST).value.toString(),
                                chatID = chatID
                            )
                            friends.add(friend)
                            Log.d("SUKA", true.toString())
                        } else {
                            val friend = FriendModel(
                                id = i.key.toString(),
                                name = i.child(NAME).value.toString(),
                                avatar = i.child(AVATAR).value.toString(),
                                incoming = i.child(FRIENDS).child(INCOMING_REQUESTS).value.toString(),
                                outgoing = i.child(FRIENDS).child(OUTGOING_REQUESTS).value.toString(),
                                friendsCount = i.child(ALCHOINFO).child(FRIENDSCOUNT)
                                    .getValue(Int::class.java)!!,
                                friendsList = i.child(FRIENDS).child(LIST).value.toString(),
                                chatID = "${currentUser.uid};${i.key}"
                            )
                            Log.d("SUKA", friend.chatID)
                            friends.add(friend)
                        }
                    }
                }
            }

            value = friends
        }

        override fun onCancelled(error: DatabaseError) {
            Log.d(TAG, "Can't listen query $query", error.toException())
        }

        private fun getListFriends(snapshot: DataSnapshot): MutableList<String> {
            val user = snapshot.child(currentUser!!.uid)
            val rawList = user.child("friends").child("list").value.toString()

            return rawList.split(";").toMutableList()
        }
    }
}
