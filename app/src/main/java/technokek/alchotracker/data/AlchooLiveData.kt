package technokek.alchotracker.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import technokek.alchotracker.data.models.AlchooCardModel

class AlchooLiveData() : MutableLiveData<MutableList<AlchooCardModel>>() {
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
            //TODO Пробегаемся по всем юзерам и заполняем и список?
            Log.d("SYKA", "ENTER")
            val alchoo: MutableList<AlchooCardModel> = mutableListOf()
            for (x in snapshot.child("users").children) {
                Log.d("SYKA", x.toString())
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("ERROR", error.toString())
        }

    }

    companion object {
        const val TAG = "AlchooLiveData"
    }
}