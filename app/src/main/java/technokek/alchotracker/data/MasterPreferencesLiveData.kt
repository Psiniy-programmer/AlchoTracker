package technokek.alchotracker.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import technokek.alchotracker.data.models.MasterPreferencesModel

class MasterPreferencesLiveData() : MutableLiveData<MutableList<MasterPreferencesModel>>() {
    private lateinit var query: Query
    private lateinit var mAuth: FirebaseAuth
    private var preferencesListener = PreferencesListener()

    constructor(query: Query, mAuth: FirebaseAuth) : this() {
        this.query = query
        this.mAuth = mAuth
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
            val preferences: MutableList<MasterPreferencesModel> = mutableListOf()
            val snap = snapshot.child(mAuth.currentUser?.uid.toString())
                .child("alchoinfo")
                .child("preferences")
                .children
            for (x in snapshot.child(mAuth.currentUser?.uid.toString())
                .child("alchoinfo")
                .child("preferences")
                .children) {
                val preferenceItem = MasterPreferencesModel(x.key.toString())
                preferences.add(preferenceItem)
            }
            value = preferences
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("ERROR", error.toString())
        }
    }

    fun addPreferenceItem(text: String) {
        query.ref.child(mAuth.currentUser?.uid.toString())
            .child("alchoinfo")
            .child("preferences")
            .child(text)
            .setValue("")
    }

    fun removePreferenceItem(text: String) {
        query.ref.child(mAuth.currentUser?.uid.toString())
            .child("alchoinfo")
            .child("preferences")
            .child(text).removeValue()
    }

    companion object {
        const val TAG = "PreferencesLiveData"
    }
}