package technokek.alchotracker.data

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import technokek.alchotracker.api.SingleChatInterface
import technokek.alchotracker.data.models.SingleChatMessageModel
import technokek.alchotracker.data.Constants.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class SingleChatLiveData() : MutableLiveData<MutableList<SingleChatMessageModel>>(),
    SingleChatInterface {
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

            for (x in snap) {
                messages.add(
                    SingleChatMessageModel(
                        x.child(ID).value.toString(),
                        x.child(DATETIME).value.toString(),
                        x.child(TEXTMESSAGE).value.toString()
                    )
                )
            }
            value = messages
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("ERROR", error.toString())
        }

    }

    @SuppressLint("SimpleDateFormat")
    override fun sendMessage(text: String) {
        val senderID = mAuth.currentUser?.uid.toString()
        val newNode = query.ref
            .child(chatID)
            .child(ALLMESSAGES)
            .child((value?.size?.plus(1)).toString())

        val sdf = SimpleDateFormat("hh:mm:ss")
        val currentDate = sdf.format(Date())

        newNode.child(ID).setValue(senderID)
        newNode.child(DATETIME).setValue(currentDate)
        newNode.child(TEXTMESSAGE).setValue(text)
    }

    companion object {
        const val TAG = "SingleChatLiveData"
    }
}