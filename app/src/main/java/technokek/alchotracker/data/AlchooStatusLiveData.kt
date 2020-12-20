package technokek.alchotracker.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import technokek.alchotracker.api.AlchooStatusInterface
import technokek.alchotracker.data.models.AlchooStatusModel
import technokek.alchotracker.data.Constants.*

class AlchooStatusLiveData() : MutableLiveData<AlchooStatusModel>(), AlchooStatusInterface {
    private lateinit var query: Query
    private lateinit var mAuth: FirebaseAuth
    private var alchooStatusListener = AlchooStatusListener()

    constructor(query: Query, mAuth: FirebaseAuth) : this() {
        this.query = query
        this.mAuth = mAuth
    }

    override fun onActive() {
        super.onActive()

        query.addValueEventListener(alchooStatusListener)
        Log.d(MasterPreferencesLiveData.TAG, "onActive")
    }

    override fun onInactive() {
        super.onInactive()

        query.removeEventListener(alchooStatusListener)
        Log.d(MasterPreferencesLiveData.TAG, "onInactive")
    }

    inner class AlchooStatusListener : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val status = snapshot
                .child(mAuth.currentUser?.uid.toString())
                .child(ALCHOINFO)
                .child(ALCHOO)
                .child(FINDER).value as Boolean
            value = AlchooStatusModel(status)
        }

        override fun onCancelled(error: DatabaseError) {
            Log.d(TAG, error.toString())
        }

    }

    companion object {
        const val TAG = "AlchooStatusLiveData"
    }

    override fun changeStatus() {
        query.ref.child(mAuth.currentUser?.uid.toString())
            .child(ALCHOINFO)
            .child(ALCHOO)
            .child(FINDER)
            .setValue(!value?.status!!)
    }
}