package technokek.alchotracker.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import technokek.alchotracker.api.AlchooInterface
import technokek.alchotracker.data.Constants.*
import technokek.alchotracker.data.models.AlchooCardModel

class AlchooLiveData() : MutableLiveData<MutableList<AlchooCardModel>>(), AlchooInterface {
    private lateinit var query: Query
    private lateinit var mAuth: FirebaseAuth
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
            for (x in snapshot.children) {
                if (x.child(ID).value.toString() != mAuth.currentUser!!.uid)
                    alchoo.add(
                        AlchooCardModel(
                            x.child(AVATAR).value.toString(),
                            x.child(NAME).value.toString(),
                            x.child(STATUS).value.toString(),
                            x.child(ID).value.toString()
                        )
                    )
            }
            value = alchoo
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("ERROR", error.toString())
        }

    }

    companion object {
        const val TAG = "AlchooLiveData"
    }

    override fun acceptBody(uid: String) {
        Log.d("SYKA", "accepted $uid")
//        TODO("Not yet implemented")
    }

    override fun declineBody(uid: String) {
        Log.d("SYKA", "declined $uid")
//        TODO("Not yet implemented")
    }
}