package technokek.alchotracker.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import technokek.alchotracker.api.AlchooInterface
import technokek.alchotracker.data.Constants.*
import technokek.alchotracker.data.models.AlchooCardModel

class AlchooLiveData() : MutableLiveData<MutableList<AlchooCardModel>>(), AlchooInterface {
    private lateinit var query: Query
    private lateinit var mAuth: FirebaseAuth
    private lateinit var curUserIncomeRequests: String
    private lateinit var curUserOutgoingRequests: String
    private lateinit var curUserDeclineList: String
    private var alchooListener = AlchooListener()

    constructor(query: Query, mAuth: FirebaseAuth) : this() {
        this.query = query
        this.mAuth = mAuth
    }

    override fun onActive() {
        super.onActive()

        query.addValueEventListener(alchooListener)
        Log.d(MasterPreferencesLiveData.TAG, "onActive")
    }

    override fun onInactive() {
        super.onInactive()

        query.removeEventListener(alchooListener)
        Log.d(MasterPreferencesLiveData.TAG, "onInactive")
    }

    inner class AlchooListener : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val alchoo: MutableList<AlchooCardModel> = mutableListOf()
            curUserIncomeRequests = snapshot
                .child(mAuth.currentUser!!.uid)
                .child(FRIENDS)
                .child(REQUESTS)
                .child(INCOMING_REQUESTS).value.toString()
            curUserOutgoingRequests = snapshot
                .child(mAuth.currentUser!!.uid)
                .child(FRIENDS)
                .child(REQUESTS)
                .child(OUTGOING_REQUESTS).value.toString()
            curUserDeclineList = snapshot
                .child(mAuth.currentUser!!.uid)
                .child(ALCHOINFO)
                .child(ALCHOO)
                .child(DECLINE_LIST).value.toString()

            if (snapshot.child(mAuth.currentUser?.uid.toString()).child(ALCHOINFO).child(ALCHOO)
                    .child(FINDER).value as Boolean
            ) {
                for (x in snapshot.children) {
                    if (x.child(ID).value.toString() != mAuth.currentUser!!.uid
                        && x.child(ALCHOINFO).child(ALCHOO).child(FINDER).value as Boolean
                        && !inFriendList(x)
                        && !inRequestsList(curUserIncomeRequests, curUserOutgoingRequests, x)
                        && !inDeclineList(x)
                    ) {
                        alchoo.add(
                            AlchooCardModel(
                                x.child(AVATAR).value.toString(),
                                x.child(NAME).value.toString(),
                                x.child(STATUS).value.toString(),
                                x.child(ID).value.toString(),
                                x.child(FRIENDS).child(INCOMING_REQUESTS).value.toString(),
                                x.child(FRIENDS).child(OUTGOING_REQUESTS).value.toString(),
                                x.child(ALCHOINFO).child(ALCHOO)
                                    .child(DECLINE_LIST).value.toString()
                            )
                        )
                    }
                }
            }
            value = alchoo
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("ERROR", error.toString())
        }

    }

    override fun acceptBody(uid: String) {
        val masterPath = getPath(mAuth.currentUser?.uid.toString(), REQUESTS)
        val friendPath = getPath(uid, REQUESTS)
        if (!curUserOutgoingRequests.isNullOrEmpty()) {
            masterPath.child(OUTGOING_REQUESTS)
                .setValue(uid)
        } else {
            masterPath.child(OUTGOING_REQUESTS)
                .setValue("${curUserOutgoingRequests};$uid")
        }

        for (item in value!!) {
            if (item.id == uid) {
                if (item.incomingRequests.isNullOrEmpty())
                    friendPath.child(INCOMING_REQUESTS)
                        .setValue(mAuth.currentUser?.uid.toString())
                else
                    friendPath.child(INCOMING_REQUESTS)
                        .setValue("${item.incomingRequests};${mAuth.currentUser?.uid.toString()}")
            }
        }
    }

    override fun declineBody(uid: String) {
        if (curUserDeclineList.isNullOrEmpty())
            query.ref.child(mAuth.currentUser?.uid.toString())
                .child(ALCHOINFO)
                .child(ALCHOO)
                .child(DECLINE_LIST)
                .setValue(uid)
        else
            query.ref.child(mAuth.currentUser?.uid.toString())
                .child(ALCHOINFO)
                .child(ALCHOO)
                .child(DECLINE_LIST)
                .setValue("${curUserDeclineList};$uid")
    }

    private fun getPath(uid: String, type: String): DatabaseReference {
        return query.ref.child(uid)
            .child(FRIENDS)
            .child(type)
    }

    private fun inFriendList(snapshot: DataSnapshot): Boolean {
        val friends = snapshot.child(FRIENDS).child(LIST).value.toString()
        return friends.contains(mAuth.currentUser?.uid.toString(), ignoreCase = true)
    }

    private fun inRequestsList(i: String, o: String, item: DataSnapshot): Boolean {
        val friendId =
            item.child(ID).value.toString()
        return (i.contains(friendId, ignoreCase = true)
                || o.contains(friendId, ignoreCase = true))
    }

    private fun inDeclineList(item: DataSnapshot): Boolean {
        val mUserId =
            item.child(ID).value.toString()
        return curUserDeclineList.contains(mUserId, ignoreCase = true)
    }

    override fun refreshList() {
        query.ref.child(mAuth.currentUser?.uid.toString())
            .child(ALCHOINFO)
            .child(ALCHOO)
            .child(DECLINE_LIST)
            .setValue("")
    }

    companion object {
        const val TAG = "AlchooLiveData"
    }
}