package technokek.alchotracker.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import technokek.alchotracker.data.models.FriendModel
import technokek.alchotracker.data.models.SearchFriendModel

class SearchFriendLiveData() : MutableLiveData<MutableList<SearchFriendModel>>() {
    private lateinit var query: Query
    private val searchFriendListener = SearchFriendListener()
    private lateinit var name: String

    constructor(ref: DatabaseReference) : this() {
        query = ref
    }

    constructor(query: Query) : this() {
        this.query = query
    }

    override fun onActive() {
        super.onActive()

        //query.addValueEventListener(searchFriendListener)
        query.addListenerForSingleValueEvent(searchFriendListener)
        Log.d(TAG, "onActive")
    }

    override fun onInactive() {
        super.onInactive()

        //query.removeEventListener(searchFriendListener)
        Log.d(TAG, "onInactive")
    }

    fun setSearchName(name: String) {
        this.name = name
        query.addListenerForSingleValueEvent(searchFriendListener)
    }

    companion object {
        const val TAG = "FriendLiveData"
    }

    inner class SearchFriendListener : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (!name.isNullOrEmpty()) {
                val usersFound: MutableList<SearchFriendModel> = mutableListOf()

                for (i in snapshot.children) {
                    if (i.child(Constants.NAME).value.toString()
                            .contains(name, ignoreCase = true)
                    ) {
                        val friend = SearchFriendModel(
                            id = i.key.toString(),
                            name = i.child(Constants.NAME).value.toString(),
                            avatar = i.child(Constants.AVATAR).value.toString()
                        )
                        usersFound.add(friend)
                    }
                }

                value = usersFound

                Log.d("SUKA", usersFound.toString())
            } else {
                value = mutableListOf()
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.d(TAG, "Can't listen query $query", error.toException())
        }
    }
}