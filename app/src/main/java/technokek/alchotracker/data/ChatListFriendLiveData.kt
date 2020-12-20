package technokek.alchotracker.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import technokek.alchotracker.data.models.SearchFriendModel

class ChatListFriendLiveData() :
    MutableLiveData<HashMap<String, MutableList<SearchFriendModel>>>() {

    private lateinit var queryForChats: Query
    private val chatListListener = ChatListListener()

    constructor(ref: DatabaseReference) : this() {
        queryForChats = ref
    }

    constructor(queryForUsers: Query) : this() {
        this.queryForChats = queryForUsers
    }

    override fun onActive() {
        super.onActive()

        queryForChats.addValueEventListener(chatListListener)
        Log.d(TAG, "onActive")
    }

    override fun onInactive() {
        super.onInactive()

        queryForChats.removeEventListener(chatListListener)
        Log.d(TAG, "onInactive")
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
                val listChats: MutableList<SearchFriendModel> = mutableListOf()

                for (j in snapshot.children) {
                    if (users.contains(j.key)) {
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
            Log.d(TAG, "Can't listen query $queryForChats", error.toException())
        }
    }
}
