package technokek.alchotracker.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import technokek.alchotracker.data.models.ChatFriendModel
import technokek.alchotracker.data.models.FriendModel
import technokek.alchotracker.data.models.SearchFriendModel

class ChatFriendLiveData() : MutableLiveData<MutableList<ChatFriendModel>>() {

    private lateinit var query: Query
    private val chatListener = ChatListener()
    private var chatID: HashMap<String, MutableList<SearchFriendModel>>? = null
    private lateinit var chatKeys: MutableSet<String>

    constructor(ref: DatabaseReference) : this() {
        query = ref
    }

    constructor(query: Query) : this() {
        this.query = query
    }

    override fun onActive() {
        super.onActive()

        query.addValueEventListener(chatListener)
        Log.d(TAG, "onActive")
    }

    override fun onInactive() {
        super.onInactive()

        query.removeEventListener(chatListener)
        Log.d(TAG, "onInactive")
    }

    fun refreshChatID(chatID: HashMap<String, MutableList<SearchFriendModel>>) {
        this.chatID = chatID
        chatKeys = chatID.keys
        query.addListenerForSingleValueEvent(chatListener)
    }

    companion object {
        const val TAG = "ChatFriendLiveData"
    }

    inner class ChatListener : ValueEventListener {
        private val mAuth = FirebaseAuth.getInstance()
        private val currentUser = mAuth.currentUser

        override fun onDataChange(snapshot: DataSnapshot) {
            if (chatID != null) {
                val chatFriends: MutableList<ChatFriendModel> = mutableListOf()
                var lastSenderID: String? = null

                for (i in snapshot.children) {
                    if (chatKeys.contains(i.key.toString())) {
                        if (!lastSenderID.isNullOrEmpty()) {
                            lastSenderID = i.child(Constants.LASTSENDERID).value.toString()

                            val list: MutableList<SearchFriendModel> =
                                chatID!!.getValue(i.key.toString())
                            val lastSearchFriend = list.filter { it.id != currentUser!!.uid }

                            Log.d("SUKA", "CRASHif: $lastSearchFriend")

                            val friend = ChatFriendModel(
                                chatID = i.key.toString(),
                                name = lastSearchFriend.first().name,
                                avatar = lastSearchFriend.first().avatar,
                                lastMessage = i.child(Constants.LASTMESSAGE).value.toString(),
                                lastDateTime = i.child(Constants.LASTDATETIME).value.toString(),
                                userID = i.child(Constants.USERS).value.toString(),
                                lastSenderID = lastSenderID
                            )
                            chatFriends.add(friend)
                        } else {
                            val list: MutableList<SearchFriendModel> =
                                chatID!!.getValue(i.key.toString())
                            val lastSearchFriend = list.filter { it.id != currentUser!!.uid }

                            Log.d("SUKA", "CRASHelse: $lastSearchFriend")

                            val friend = ChatFriendModel(
                                chatID = i.key.toString(),
                                name = lastSearchFriend.first().name,
                                avatar = lastSearchFriend.first().avatar,
                                lastMessage = i.child(Constants.LASTMESSAGE).value.toString(),
                                lastDateTime = i.child(Constants.LASTDATETIME).value.toString(),
                                userID = i.child(Constants.USERS).value.toString(),
                                lastSenderID = "lastSenderID"
                            )
                            chatFriends.add(friend)
                        }
                    }
                }

                value = chatFriends
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.d(TAG, "Can't listen query $query", error.toException())
        }
    }
}