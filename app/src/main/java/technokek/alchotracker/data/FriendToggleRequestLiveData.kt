package technokek.alchotracker.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import technokek.alchotracker.data.models.FriendToggleRequestModel
import technokek.alchotracker.data.Constants.*

class FriendToggleRequestLiveData() : MutableLiveData<FriendToggleRequestModel>() {
    private lateinit var uid: String
    private lateinit var query: Query
    private lateinit var mAuth: FirebaseAuth
    private var requestsListener = RequestsListener()

    constructor(query: Query, mAuth: FirebaseAuth, uid: String) : this() {
        this.query = query
        this.mAuth = mAuth
        this.uid = uid
    }

    override fun onActive() {
        super.onActive()

        query.addValueEventListener(requestsListener)
        Log.d(MasterPreferencesLiveData.TAG, "onActive")
    }

    override fun onInactive() {
        super.onInactive()

        query.removeEventListener(requestsListener)
        Log.d(MasterPreferencesLiveData.TAG, "onInactive")
    }

    inner class RequestsListener : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val x = snapshot.child(mAuth.currentUser?.uid.toString())
                .child(FRIENDS)
            val y = snapshot.child(uid)
                .child(FRIENDS)
            val masterFriendList = x.child(LIST)
                .value.toString()
            val masterIncList = x.child(REQUESTS)
                .child(INCOMING_REQUESTS)
                .value.toString()
            val masterOutList = x.child(REQUESTS)
                .child(OUTGOING_REQUESTS)
                .value.toString()
            val friendIncList = y.child(INCOMING_REQUESTS)
                .child(REQUESTS)
                .value.toString()
            val friendOutList = y.child(OUTGOING_REQUESTS)
                .child(REQUESTS)
                .value.toString()

            if (masterFriendList.split(";").contains(uid)) {
                value = FriendToggleRequestModel(
                    inFriend = true,
                    requestIsSended = false,
                    masterOutList,
                    masterIncList,
                    friendOutList,
                    friendIncList,
                    x.child(LIST).value.toString(),
                    y.child(LIST).value.toString()
                )
            } else if (masterOutList.split(";").contains(uid)) {
                value = FriendToggleRequestModel(
                    inFriend = false,
                    requestIsSended = true,
                    masterOutList,
                    masterIncList,
                    friendOutList,
                    friendIncList,
                    x.child(LIST).value.toString(),
                    y.child(LIST).value.toString()
                )
            } else {
                value = FriendToggleRequestModel(
                    inFriend = false,
                    requestIsSended = false,
                    masterOutList,
                    masterIncList,
                    friendOutList,
                    friendIncList,
                    x.child(LIST).value.toString(),
                    y.child(LIST).value.toString()
                )
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("ERROR", error.toString())
        }
    }

    fun addFriend() {
        val masterPath = getPath(mAuth.currentUser?.uid.toString(), REQUESTS)
        val friendPath = getPath(uid, REQUESTS)
        if (value?.masterOutReq.isNullOrEmpty()) {
            masterPath.child(OUTGOING_REQUESTS)
                .setValue(uid)
        } else {
            masterPath.child(OUTGOING_REQUESTS)
                .setValue("${value?.masterOutReq};$uid")
        }
        if (!value?.friendInReq.isNullOrEmpty()) {
            friendPath.child(INCOMING_REQUESTS)
                .setValue(mAuth.currentUser?.uid.toString())
        } else {
            friendPath.child(INCOMING_REQUESTS)
                .setValue("${value?.friendInReq};${mAuth.currentUser?.uid}")
        }
    }

    fun deleteFriend() {
        val masterPath = getPath(mAuth.currentUser?.uid.toString(), LIST)
        val friendPath = getPath(uid, LIST)
        val friendList =
            value?.friendList?.split(";")?.filter { it != mAuth.currentUser?.uid.toString() }
        val masterList = value?.masterList?.split(";")?.filter { it != uid }
        val masterStr = masterList?.let { setStr(it) }
        val friendStr = friendList?.let { setStr(it) }
        masterPath.setValue(masterStr)
        friendPath.setValue(friendStr)
    }

    private fun setStr(list: List<String>): String {
        var res = String()
        list.map {
            if (res.isEmpty()) res = it
            else res += it
        }
        return res
    }

    private fun getPath(uid: String, type: String): DatabaseReference {
        return query.ref.child(uid)
            .child(FRIENDS)
            .child(type)
    }

    fun cancelRequest() {
        // TODO("cancelRequest")
    }

    companion object {
        const val TAG = "FriendToggleRequestLiveData"
    }
}
