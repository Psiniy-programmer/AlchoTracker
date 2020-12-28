package technokek.alchotracker.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import technokek.alchotracker.data.models.FriendModel

class MembersLiveData() : MutableLiveData<MutableList<FriendModel>>() {

    private lateinit var queryForUsers: Query
    private lateinit var queryForEvents: Query
    private lateinit var eventID: String
    private var listMembers: MutableList<String> = mutableListOf()
    private val listenerForUsers: UsersListener = UsersListener()
    private val listenerForEvents: EventsListener = EventsListener()

    constructor(
        queryForUsers: Query,
        queryForEvents: Query,
        eventID: String
    ) : this() {
        this.queryForUsers = queryForUsers
        this.queryForEvents = queryForEvents
        this.eventID = eventID
    }

    constructor(
        queryForUsers: DatabaseReference,
        queryForEvents: DatabaseReference,
        eventID: String
    ) : this() {
        this.queryForUsers = queryForUsers
        this.queryForEvents = queryForEvents
        this.eventID = eventID
    }

    override fun onActive() {
        super.onActive()

        queryForEvents.addValueEventListener(listenerForEvents)
    }

    override fun onInactive() {
        super.onInactive()

        queryForEvents.removeEventListener(listenerForEvents)
    }

    companion object {
        const val TAG = "MembersLiveData"
    }

    inner class UsersListener : ValueEventListener {

        override fun onDataChange(snapshot: DataSnapshot) {
            if (!listMembers.isNullOrEmpty()) {
                val listFriends: MutableList<FriendModel> = mutableListOf()
                for (x in snapshot.children) {
                    if (listMembers.contains(x.key.toString())) {
                        val member = FriendModel(
                            id = x.key.toString(),
                            name = x.child(Constants.NAME).value.toString(),
                            avatar = x.child(Constants.AVATAR).value.toString(),
                            incoming = x.child(Constants.FRIENDS).child(Constants.INCOMING_REQUESTS).value.toString(),
                            outgoing = x.child(Constants.FRIENDS).child(Constants.OUTGOING_REQUESTS).value.toString(),
                            friendsCount = x.child(Constants.ALCHOINFO).child(Constants.FRIENDSCOUNT)
                                .getValue(Int::class.java)!!,
                            friendsList = x.child(Constants.FRIENDS).child(Constants.LIST).value.toString()
                        )
                        listFriends.add(member)
                    }
                }

                value = listFriends
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.d(FriendLiveData.TAG, "Can't listen query $queryForUsers", error.toException())
        }
    }

    inner class EventsListener : ValueEventListener {

        override fun onDataChange(snapshot: DataSnapshot) {
            val x = snapshot.child(eventID).child(Constants.MEMBERS)
            listMembers.add(x.child(Constants.ADMIN_ID).value.toString())
            val list: List<String> = x.child(Constants.USERS).value.toString().split(";")
            listMembers.addAll(list)

            queryForUsers.addListenerForSingleValueEvent(listenerForUsers)
        }

        override fun onCancelled(error: DatabaseError) {
            Log.d(FriendLiveData.TAG, "Can't listen query $queryForEvents", error.toException())
        }
    }
}
