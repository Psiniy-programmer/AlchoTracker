package technokek.alchotracker.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import technokek.alchotracker.api.ChatInterFace
import technokek.alchotracker.data.models.FriendModel
import technokek.alchotracker.data.models.SearchFriendModel

class ChatListFriendLiveData() :
    MutableLiveData<HashMap<String, MutableList<SearchFriendModel>>>(),
    ChatInterFace {

    private lateinit var queryForUsers: Query
    private val chatListListener = ChatListListener()

    constructor(ref: DatabaseReference) : this() {
        queryForUsers = ref
    }

    constructor(queryForUsers: Query) : this() {
        this.queryForUsers = queryForUsers
    }

    override fun onActive() {
        super.onActive()

        queryForUsers.addValueEventListener(chatListListener)
        Log.d(TAG, "onActive")
    }

    override fun onInactive() {
        super.onInactive()

        queryForUsers.removeEventListener(chatListListener)
        Log.d(TAG, "onInactive")
    }

    override fun createChat(model: FriendModel) {
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (model.bool) {
            queryForUsers.ref.child(currentUser!!.uid).child(Constants.CHATID).child(model.chatID)
                .apply {
                    child(Constants.USERS).setValue(model.chatID)
                }
            queryForUsers.ref.child(model.id).child(Constants.CHATID).child(model.chatID).apply {
                child(Constants.USERS).setValue(model.chatID)
            }
        }
    }

    companion object {
        const val TAG = "ChatListFriendLiveData"
    }

    inner class ChatListListener : ValueEventListener {
        private val mAuth = FirebaseAuth.getInstance()
        private val currentUser = mAuth.currentUser

        override fun onDataChange(snapshot: DataSnapshot) {
            val chatHashMap: HashMap<String, MutableList<SearchFriendModel>> = hashMapOf()
            val chatCurrentUser = snapshot.child(currentUser!!.uid).child(Constants.CHATID)

            for (i in chatCurrentUser.children) {
                val users: List<String> = i.child(Constants.USERS).value.toString().split(";")
                val usersWithoutYou = users.filter { it != currentUser.uid }
                val listChats: MutableList<SearchFriendModel> = mutableListOf()

                for (j in snapshot.children) {

                    if (usersWithoutYou.contains(j.key.toString())) {
                        val itemList = SearchFriendModel(
                            id = j.key.toString(),
                            name = j.child(Constants.NAME).value.toString(),
                            avatar = j.child(Constants.AVATAR).value.toString()
                        )

                        listChats.add(itemList)
                    } else if (j.key == currentUser.uid) {
                        val itemList = SearchFriendModel(
                            id = j.key.toString(),
                            name = j.child(Constants.NAME).value.toString(),
                            avatar = j.child(Constants.AVATAR).value.toString()
                        )

                        listChats.add(itemList)
                    }
                }

                chatHashMap[i.key.toString()] = listChats
            }

            value = chatHashMap
        }

        override fun onCancelled(error: DatabaseError) {
            Log.d(TAG, "Can't listen query $queryForUsers", error.toException())
        }
    }
}
