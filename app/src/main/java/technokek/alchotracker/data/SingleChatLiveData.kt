package technokek.alchotracker.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import technokek.alchotracker.api.ChatInterface
import technokek.alchotracker.data.models.SingleChatMessageModel
import technokek.alchotracker.data.Constants.*


class SingleChatLiveData() : MutableLiveData<MutableList<SingleChatMessageModel>>(), ChatInterface {
    private lateinit var query: Query
    private lateinit var mAuth: FirebaseAuth
    private lateinit var chatID: String
    private val singleChatListener = SingleChatListener()

    constructor(query: Query, mAuth: FirebaseAuth, chatId: String) : this() {
        this.query = query
        this.mAuth = mAuth
        this.chatID = chatId
    }

    override fun onActive() {
        super.onActive()

        query.addValueEventListener(singleChatListener)
        Log.d(MasterProfileLiveData.TAG, "onActive")
    }

    override fun onInactive() {
        super.onInactive()

        query.removeEventListener(singleChatListener)
        Log.d(MasterProfileLiveData.TAG, "onInactive")
    }

    inner class SingleChatListener : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val messages: MutableList<SingleChatMessageModel> = mutableListOf()
            val snap = snapshot.child(chatID).child(ALLMESSAGES).children
            Log.d("SYKA", snapshot.child(chatID).child(ALLMESSAGES).value.toString())
//            TODO("Not yet implemented")

            for (x in snap) {
                messages.add(SingleChatMessageModel(
                    x.child(ID).value.toString(),
                    x.child(DATETIME).value.toString(),
                    x.child(TEXTMESSAGE).value.toString()))
            }

            for (i in 0 until messages.size)
                Log.d("SYKA", messages[i].message)
            value = messages
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("ERROR", error.toString())
        }

    }

    override fun sendMessage(text: String) {
        //        TODO("Not yet implemented")
        Log.d("SYKA", "senderWorking")
    }

    companion object {
        const val TAG = "SingleChatLiveData"
    }
}