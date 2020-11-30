package technokek.alchotracker.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import technokek.alchotracker.data.models.FriendToggleRequestModel

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
                .child("friends")
            val y = snapshot.child(uid)
                .child("friends")
            val masterFriendList = x.child("list")
                .value.toString()
            val masterIncList = x.child("requests")
                .child("incoming")
                .value.toString()
            val masterOutList = x.child("requests")
                .child("outgoing")
                .value.toString()
            val friendIncList = y.child("incoming")
                .child("requests")
                .value.toString()
            val friendOutList = y.child("outgoing")
                .child("requests")
                .value.toString()

            if (masterFriendList.split(";").contains(uid)) {
                value = FriendToggleRequestModel(
                    inFriend = true,
                    requestIsSended = false,
                    masterOutList,
                    masterIncList,
                    friendOutList,
                    friendIncList,
                    x.child("list").value.toString(),
                    y.child("list").value.toString()
                )
            } else if (masterOutList.split(";").contains(uid)) {
                value = FriendToggleRequestModel(
                    inFriend = false,
                    requestIsSended = true,
                    masterOutList,
                    masterIncList,
                    friendOutList,
                    friendIncList,
                    x.child("list").value.toString(),
                    y.child("list").value.toString()
                )
            } else {
                value = FriendToggleRequestModel(
                    inFriend = false,
                    requestIsSended = false,
                    masterOutList,
                    masterIncList,
                    friendOutList,
                    friendIncList,
                    x.child("list").value.toString(),
                    y.child("list").value.toString()
                )
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("ERROR", error.toString())
        }

    }

    fun addFriend() {
        val masterPath = query.ref.child(mAuth.currentUser?.uid.toString())
            .child("friends")
            .child("requests")
        val friendPath = query.ref.child(uid)
            .child("friends")
            .child("requests")
        if (value?.masterOutReq?.length == 0) {
            masterPath.child("outgoing")
                .setValue(uid)
        } else {
            masterPath.child("outgoing")
                .setValue("${value?.masterOutReq};$uid")
        }
        if (value?.friendOutReq?.length == 0) {
            friendPath.child("incoming")
                .setValue(mAuth.currentUser?.uid.toString())
        } else {
            friendPath.child("incoming")
                .setValue("${value?.friendInReq};${mAuth.currentUser?.uid.toString()}")
        }
    }

    fun deleteFriend() {
        val masterPath = query.ref.child(mAuth.currentUser?.uid.toString())
            .child("friends")
            .child("list")
        val friendPath = query.ref.child(uid)
            .child("friends")
            .child("list")
        val friendList =
            value?.friendList?.split(";")?.filter { it != mAuth.currentUser?.uid.toString() }
        val masterList = value?.masterList?.split(";")?.filter { it != uid }
        var masterStr = ""
        var friendStr = ""
        friendList?.map {
            if( friendStr == "" ) friendStr = it
            else friendStr += ";$it"
        }
        masterList?.map {
            if( masterStr == "" ) masterStr = it
            else masterStr += ";$it"
        }
        masterPath.setValue(masterStr)
        friendPath.setValue(friendStr)
    }

    fun cancelRequest() {
        //TODO("cancelRequest")
    }

    fun getNewStr(str: String): String {
        return str
    }

    companion object {
        const val TAG = "FriendToggleRequestLiveData"
    }
}