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
    private lateinit var masterOutReq: String
    private lateinit var masterInReq: String
    private lateinit var masterList: String
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
            masterOutReq =  x.child(REQUESTS)
                .child(OUTGOING_REQUESTS)
                .value.toString()
            masterInReq = x.child(REQUESTS)
                .child(INCOMING_REQUESTS)
                .value.toString()
            masterList = x.child(REQUESTS)
                .child(OUTGOING_REQUESTS)
                .value.toString()

            val y = snapshot.child(uid)
                .child(FRIENDS)
            
            val friendIncList = y.child(INCOMING_REQUESTS)
                .child(REQUESTS)
                .value.toString()
            val friendOutList = y.child(OUTGOING_REQUESTS)
                .child(REQUESTS)
                .value.toString()

            if (masterList.split(";").contains(uid)) {
                value = FriendToggleRequestModel(
                    inFriend = true,
                    requestIsSended = false,
                    friendOutList,
                    friendIncList,
                    y.child(LIST).value.toString()
                )
            } else if (masterOutReq.split(";").contains(uid)) {
                value = FriendToggleRequestModel(
                    inFriend = false,
                    requestIsSended = true,
                    friendOutList,
                    friendIncList,
                    y.child(LIST).value.toString()
                )
            } else {
                value = FriendToggleRequestModel(
                    inFriend = false,
                    requestIsSended = false,
                    friendOutList,
                    friendIncList,
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
        if (masterOutReq.isNullOrEmpty()) {
            masterPath.child(OUTGOING_REQUESTS)
                .setValue(uid)
        } else {
            masterPath.child(OUTGOING_REQUESTS)
                .setValue("${masterOutReq};$uid")
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
        val mMasterList = masterList.split(";").filter { it != uid }
        val masterStr = setStr(mMasterList)
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
