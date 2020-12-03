package technokek.alchotracker.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import technokek.alchotracker.data.models.FriendPreferencesModel

class FriendPreferencesLiveData() : MutableLiveData<MutableList<FriendPreferencesModel>>() {
    private lateinit var uid: String
    private lateinit var query: Query
    private var preferencesListener = PreferencesListener()

    constructor(query: Query, uid: String) : this() {
        this.query = query
        this.uid = uid
    }

    override fun onActive() {
        super.onActive()

        query.addValueEventListener(preferencesListener)
        Log.d(MasterPreferencesLiveData.TAG, "onActive")
    }

    override fun onInactive() {
        super.onInactive()

        query.removeEventListener(preferencesListener)
        Log.d(MasterPreferencesLiveData.TAG, "onInactive")
    }

    inner class PreferencesListener : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val preferences: MutableList<FriendPreferencesModel> = mutableListOf()
            val snap = snapshot.child(uid)
                .child("alchoinfo")
                .child("preferences")
                .children
            for (x in snap) {
                val preferenceItem = FriendPreferencesModel(x.key.toString())
                preferences.add(preferenceItem)
            }
            value = preferences
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("ERROR", error.toString())
        }
    }

    companion object {
        const val TAG = "FriendPreferencesLiveData"
    }
}
