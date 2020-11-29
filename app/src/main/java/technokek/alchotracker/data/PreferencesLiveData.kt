package technokek.alchotracker.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
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
        Log.d(PreferencesLiveData.TAG, "onActive")
    }

    override fun onInactive() {
        super.onInactive()

        query.removeEventListener(preferencesListener)
        Log.d(PreferencesLiveData.TAG, "onInactive")
    }

    inner class PreferencesListener : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {

        }

        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }

    }

    companion object {
        const val TAG = "PreferencesLiveData"
    }
}