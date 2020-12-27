package technokek.alchotracker.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import technokek.alchotracker.data.models.FriendModel

class ChatCreateDialog() : MutableLiveData<MutableList<FriendModel>>() {

    private lateinit var query: Query
    private val listFriendListener = ListFriendListener()

    constructor(query: Query) : this() {
        this.query = query
    }

    constructor(ref: DatabaseReference) : this() {
        query = ref
    }

    override fun onActive() {
        super.onActive()

        query.addValueEventListener(listFriendListener)
        Log.d(TAG, "onActive")
    }

    override fun onInactive() {
        super.onInactive()

        query.removeEventListener(listFriendListener)
        Log.d(TAG, "onInactive")
    }

    companion object {
        const val TAG = "ChatCreateDialog"
    }

    inner class ListFriendListener : ValueEventListener {
        private val mAuth = FirebaseAuth.getInstance()
        private val currentUser = mAuth.currentUser

        override fun onDataChange(snapshot: DataSnapshot) {
            val friends: MutableList<FriendModel> = mutableListOf()
            val listFriend: MutableList<String> = getListFriends(snapshot)

            val listChatID: MutableList<String> = mutableListOf()
            val snap = snapshot
                .child(currentUser!!.uid)
                .child(Constants.CHATID)
            for (i in snap.children) {
                listChatID.add(i.key.toString())
            }

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
                    }

                    if (bool) {
                        val friend = FriendModel(
                            id = i.key.toString(),
                            name = i.child(Constants.NAME).value.toString(),
                            avatar = i.child(Constants.AVATAR).value.toString(),
                            incoming = i.child(Constants.FRIENDS)
                                .child(Constants.INCOMING_REQUESTS).value.toString(),
                            outgoing = i.child(Constants.FRIENDS)
                                .child(Constants.OUTGOING_REQUESTS).value.toString(),
                            friendsCount = i.child(Constants.ALCHOINFO)
                                .child(Constants.FRIENDSCOUNT)
                                .getValue(Int::class.java)!!,
                            friendsList = i.child(Constants.FRIENDS)
                                .child(Constants.LIST).value.toString(),
                            chatID = chatID,
                            bool = false
                        )
                        friends.add(friend)
                    } else {
                        val friend = FriendModel(
                            id = i.key.toString(),
                            name = i.child(Constants.NAME).value.toString(),
                            avatar = i.child(Constants.AVATAR).value.toString(),
                            incoming = i.child(Constants.FRIENDS)
                                .child(Constants.INCOMING_REQUESTS).value.toString(),
                            outgoing = i.child(Constants.FRIENDS)
                                .child(Constants.OUTGOING_REQUESTS).value.toString(),
                            friendsCount = i.child(Constants.ALCHOINFO)
                                .child(Constants.FRIENDSCOUNT)
                                .getValue(Int::class.java)!!,
                            friendsList = i.child(Constants.FRIENDS)
                                .child(Constants.LIST).value.toString(),
                            chatID = "${currentUser.uid};${i.key}",
                            bool = true
                        )
                        friends.add(friend)
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
