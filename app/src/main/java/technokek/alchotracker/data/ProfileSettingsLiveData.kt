package technokek.alchotracker.data

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import technokek.alchotracker.api.ProfileSettingsInterface
import technokek.alchotracker.data.models.SettingsProfileModel
import technokek.alchotracker.data.Constants.*
import java.io.ByteArrayOutputStream
import java.io.IOException

class ProfileSettingsLiveData() : MutableLiveData<SettingsProfileModel>() {
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

    fun setStatus(newStatus: String) {
        query.ref.child(mAuth.currentUser?.uid.toString())
            .child(STATUS)
            .setValue(newStatus)
    }

    fun setAvatar(newAvatar: Bitmap) {
        val uid = mAuth.currentUser?.uid.toString()
        val ref = storage.child("img").child(uid)
        val baos = ByteArrayOutputStream()
        newAvatar.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val byteArray: ByteArray = baos.toByteArray()
        val uploadTask: UploadTask = ref.putBytes(byteArray)

        uploadTask.continueWithTask{task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            ref.downloadUrl}.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                query.ref.child(uid).child(AVATAR).setValue(downloadUri.toString())
            }
        }
    }

    fun setDrink(newDrink: String) {
        query.ref.child(mAuth.currentUser?.uid.toString())
            .child(ALCHOINFO)
            .child(FAVOURITEDRINK)
            .setValue(newDrink)
    }

    fun onAlchoo() {
        query.ref.child(mAuth.currentUser?.uid.toString())
            .child(ALCHOINFO)
            .child(ALCHOO)
            .child(FINDER)
            .setValue(true)
    }

    fun offAlchoo() {
        query.ref.child(mAuth.currentUser?.uid.toString())
            .child(ALCHOINFO)
            .child(ALCHOO)
            .child(FINDER)
            .setValue(false)
    }

    fun signOut() {
        mAuth.signOut()
    }

    companion object {
        const val TAG = "ProfileSettingsLiveData"
    }
}
