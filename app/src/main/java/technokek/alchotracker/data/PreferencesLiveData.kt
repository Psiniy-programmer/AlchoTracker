package technokek.alchotracker.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import technokek.alchotracker.data.models.PreferencesModel

class PreferencesLiveData() : MutableLiveData<MutableList<PreferencesModel>>() {
    private lateinit var query: Query
    private var preferencesListener = PreferencesListener()

    constructor(query: Query) : this() {
        this.query = query
    }

    override fun onActive() {
        super.onActive()

        query.addValueEventListener(preferencesListener)
        Log.d(TAG, "onActive")
    }

    override fun onInactive() {
        super.onInactive()

        query.removeEventListener(preferencesListener)
        Log.d(TAG, "onInactive")
    }

    inner class PreferencesListener : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val preferences: MutableList<PreferencesModel> = mutableListOf()
            val mAuth = FirebaseAuth.getInstance()
            for (x in snapshot.child(mAuth.currentUser?.uid.toString()).child("alchoinfo")
                .child("preferences").children) {
                val preferenceItem = PreferencesModel(x.value.toString())
                preferences.add(preferenceItem)
            }

            value = preferences
        }

        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }

    }

    companion object {
        const val TAG = "PreferencesLiveData"
    }
}