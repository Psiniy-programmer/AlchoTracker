package technokek.alchotracker.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import technokek.alchotracker.data.models.FriendModel
import technokek.alchotracker.data.repositories.UserRepository

class AuthLiveData(private val query: DatabaseReference) : MutableLiveData<FriendModel>() {

    private val authListener = AuthListener()

    override fun onActive() {
        super.onActive()

        query.addValueEventListener(authListener)
        Log.d(TAG, "onActive")
    }

    override fun onInactive() {
        super.onInactive()

        query.removeEventListener(authListener)
        Log.d(TAG, "onInactive")
    }

    companion object {
        const val TAG = "AuthLiveData"
    }

    inner class AuthListener : ValueEventListener {
        private val mAuth = FirebaseAuth.getInstance()
        private val currentUser = mAuth.currentUser

        override fun onDataChange(snapshot: DataSnapshot) {
            var friend: FriendModel? = null

            for (i in snapshot.children) {
                if (i.key == currentUser!!.uid) {
                    friend = FriendModel(
                        id = i.key.toString(),
                        name = i.child("name").value.toString(),
                        avatar = i.child("avatar").value.toString(),
                        incoming = i.child("friends").child("incoming").value.toString(),
                        outgoing = i.child("friends").child("outgoing").value.toString(),
                        friendsCount = i.child("alchoinfo").child("friendsCount")
                            .getValue(Int::class.java)!!,
                        friendsList = i.child("friends").child("list").value.toString()
                    )

                    break
                }
            }

            value = friend
        }

        override fun onCancelled(error: DatabaseError) {
            Log.d(FriendLiveData.TAG, "Can't listen query $query", error.toException())
        }
    }
}
