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
import technokek.alchotracker.data.models.SettingsProfileModel

class ProfileSettingsLiveData() : MutableLiveData<SettingsProfileModel>() {
    private lateinit var query: Query
    private lateinit var storage: StorageReference
    private var settingsListener = SettingsListener()
    private var mAuth = FirebaseAuth.getInstance()

    constructor(query: Query, storage: StorageReference) : this() {
        this.query = query
        this.storage = storage
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
            Log.d("ERROR_MSG", error.toString())
        }
    }

    fun getModel(x: DataSnapshot) {
        value = SettingsProfileModel(
            x.child("avatar").value.toString(),
            x.child("status").value.toString(),
            x.child("alchoinfo").child("favouriteDrink").value.toString()
        )
    }

    fun setStatus(newStatus: String) {
        value?.status = newStatus
        query.ref.child(mAuth.currentUser?.uid.toString()).child("status").setValue(newStatus)
    }

    fun setAvatar(newAvatar: Uri) {

        storage.putFile(newAvatar).addOnCompleteListener { putTask ->
            if (putTask.isSuccessful) {
                storage.downloadUrl.addOnCompleteListener { downloadTask ->
                    if (downloadTask.isSuccessful) {
                        val avatarUrl = downloadTask.result.toString()
                        Log.d("KEKTEST", avatarUrl)
                        query.ref.child(mAuth.currentUser?.uid.toString()).child("avatar")
                            .setValue(avatarUrl)
                    }
                }
            } else Log.d("KEKTEST", putTask.exception.toString())
        }
    }

    fun setDrink(newDrink: String) {
        value?.drink = newDrink
        query.ref.child(mAuth.currentUser?.uid.toString()).child("alchoinfo")
            .child("favouriteDrink").setValue(newDrink)
    }

    fun signOut() {
        mAuth.signOut()
    }

    companion object {
        const val TAG = "ProfileSettingsLiveData"
    }
}