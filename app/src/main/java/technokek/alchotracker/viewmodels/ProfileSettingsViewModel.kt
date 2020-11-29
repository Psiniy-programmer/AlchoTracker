package technokek.alchotracker.viewmodels

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import technokek.alchotracker.data.ProfileSettingsLiveData
import technokek.alchotracker.data.models.SettingsProfileModel

class ProfileSettingsViewModel(application: Application) : AndroidViewModel(application) {
    private var profileSettings = ProfileSettingsLiveData(dbRef, sRef)
    private val mMediatorLiveData = MediatorLiveData<SettingsProfileModel>()

    init {
        mMediatorLiveData.addSource(profileSettings) {
            if (it != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    mMediatorLiveData.postValue(it)
                }
            } else {
                mMediatorLiveData.value = null
            }
        }
    }

    fun setStatus(newStatus: String) {
        CoroutineScope(Dispatchers.IO).launch {
            profileSettings.setStatus(newStatus)
        }
    }

    fun setAvatar(newAvatar: Uri) {
        CoroutineScope(Dispatchers.IO).launch {
            profileSettings.setAvatar(newAvatar)
        }
    }

    fun setDrink(newDrink: String) {
        CoroutineScope(Dispatchers.IO).launch {
            profileSettings.setDrink(newDrink)
        }
    }

    fun signOut() {
        profileSettings.signOut()
    }

    companion object {
        private val dbRef = FirebaseDatabase.getInstance().getReference("users")
        private val sRef = FirebaseStorage.getInstance().reference
    }
}