package technokek.alchotracker.data

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference
import technokek.alchotracker.api.ProfileSettingsInterface
import technokek.alchotracker.data.models.SettingsProfileModel
import technokek.alchotracker.data.Constants.*

class ProfileSettingsLiveData() : MutableLiveData<SettingsProfileModel>(), ProfileSettingsInterface {
    private lateinit var query: Query
    private lateinit var storage: StorageReference
    private lateinit var mAuth: FirebaseAuth
    private var settingsListener = SettingsListener()

    constructor(query: Query, storage: StorageReference, mAuth: FirebaseAuth) : this() {
        this.query = query
        this.storage = storage
        this.mAuth = mAuth
    }

    override fun onActive() {
        super.onActive()

        query.addValueEventListener(settingsListener)
        Log.d(TAG, "onActive")
    }

    override fun onInactive() {
        super.onInactive()

        query.removeEventListener(settingsListener)
        Log.d(TAG, "onInactive")
    }

    inner class SettingsListener : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {

            getModel(snapshot.child(mAuth.currentUser?.uid.toString()))
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("ERROR_MSG", error.toString())
        }
    }

    fun getModel(x: DataSnapshot) {
        value = SettingsProfileModel(
            x.child(AVATAR).value.toString(),
            x.child(STATUS).value.toString(),
            x.child(ALCHOINFO).child(FAVOURITEDRINK).value.toString(),
            x.child(ALCHOINFO).child(ALCHOO).child(FINDER).value as Boolean
        )
    }

    override fun setStatus(newStatus: String) {
        query.ref.child(mAuth.currentUser?.uid.toString())
            .child(STATUS)
            .setValue(newStatus)
    }

    override fun setAvatar(newAvatar: Uri) {

        storage.putFile(newAvatar).addOnCompleteListener { putTask ->
            if (putTask.isSuccessful) {
                storage.downloadUrl.addOnCompleteListener { downloadTask ->
                    if (downloadTask.isSuccessful) {
                        val avatarUrl = downloadTask.result.toString()
                        query.ref.child(mAuth.currentUser?.uid.toString()).child(AVATAR)
                            .setValue(avatarUrl)
                    }
                }
            } else Log.e("ERROR", putTask.exception.toString())
        }
    }

    override fun setDrink(newDrink: String) {
        query.ref.child(mAuth.currentUser?.uid.toString())
            .child(ALCHOINFO)
            .child(FAVOURITEDRINK)
            .setValue(newDrink)
    }

    override fun onAlchoo() {
        query.ref.child(mAuth.currentUser?.uid.toString())
            .child(ALCHOINFO)
            .child(ALCHOO)
            .child(FINDER)
            .setValue(true)
    }

    override fun offAlchoo() {
        query.ref.child(mAuth.currentUser?.uid.toString())
            .child(ALCHOINFO)
            .child(ALCHOO)
            .child(FINDER)
            .setValue(false)
    }

    override fun signOut() {
        mAuth.signOut()
    }

    companion object {
        const val TAG = "ProfileSettingsLiveData"
    }
}
