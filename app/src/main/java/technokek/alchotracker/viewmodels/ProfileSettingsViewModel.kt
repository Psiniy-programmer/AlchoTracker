package technokek.alchotracker.viewmodels

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import technokek.alchotracker.api.ProfileSettingsInterface
import technokek.alchotracker.data.ProfileSettingsLiveData
import technokek.alchotracker.data.models.SettingsProfileModel

class ProfileSettingsViewModel(application: Application) : AndroidViewModel(application), ProfileSettingsInterface {
    private var profileSettings = ProfileSettingsLiveData(dbRef, sRef, aRef)
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

    override fun setStatus(newStatus: String) {
        CoroutineScope(Dispatchers.IO).launch {
            profileSettings.setStatus(newStatus)
        }
    }

    override fun setAvatar(newAvatar: Uri) {
        CoroutineScope(Dispatchers.IO).launch {
            profileSettings.setAvatar(newAvatar)
        }
    }

    override fun setDrink(newDrink: String) {
        CoroutineScope(Dispatchers.IO).launch {
            profileSettings.setDrink(newDrink)
        }
    }

    override fun signOut() {
        profileSettings.signOut()
    }

    companion object {
        private val dbRef = FirebaseDatabase.getInstance().getReference("users")
        private val sRef = FirebaseStorage.getInstance().reference
        private val aRef = FirebaseAuth.getInstance()
    }
}
