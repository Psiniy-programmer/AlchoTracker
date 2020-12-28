package technokek.alchotracker.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import technokek.alchotracker.data.Constants.*
import technokek.alchotracker.data.models.FriendProfileModel

class FriendProfileLiveData() : MutableLiveData<FriendProfileModel>() {
    private lateinit var uid: String
    private lateinit var query: Query
    private lateinit var aRef: FirebaseAuth
    private val profileListener = ProfileListener()

    constructor(query: Query, aRef: FirebaseAuth, uid: String) : this() {
        this.query = query
        this.uid = uid
        this.aRef = aRef
    }

    override fun onActive() {
        super.onActive()

        query.addValueEventListener(profileListener)
        Log.d(TAG, "onActive")
    }

    override fun onInactive() {
        super.onInactive()

        query.removeEventListener(profileListener)
        Log.d(TAG, "onInactive")
    }

    inner class ProfileListener : ValueEventListener {

        override fun onDataChange(snapshot: DataSnapshot) {
            val chats = snapshot.child(uid).child(CHATID).children
            var chatID = String()
            for (item in chats) {
                if (item.key?.contains(
                        aRef.currentUser?.uid.toString(),
                        ignoreCase = true
                    ) == true
                ) {
                    chatID = item.key!!
                }
            }
            setChatID(chatID)
            setProfile(snapshot.child(uid))
            value?.let { Log.d("SYKA", it.chatID) }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("error", error.toString())
        }
    }

    fun setProfile(x: DataSnapshot) {

        value = FriendProfileModel(
            x.child(AVATAR).value.toString(),
            x.child(NAME).value.toString(),
            x.child(STATUS).value.toString(),
            x.child(ALCHOINFO).child(FRIENDSCOUNT).getValue(Int::class.java)!!,
            x.child(ALCHOINFO).child(EVENTSCOUNT).getValue(Int::class.java)!!,
            x.child(ALCHOINFO).child(FAVOURITEDRINK).value.toString(),
            x.child(CHATID).value.toString()
        )
    }

    fun setChatID(id: String) {
        if (id.isNotEmpty())
            query.ref.child(aRef.currentUser?.uid.toString())
                .child(CHATID).setValue(id)
        else
            query.ref.child(aRef.currentUser?.uid.toString())
                .child(CHATID).setValue("none")
    }

    companion object {
        const val TAG = "FriendProfileLiveData"
    }
}
