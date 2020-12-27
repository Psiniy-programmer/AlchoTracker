package technokek.alchotracker.viewmodels

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
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
import java.io.IOException

class ProfileSettingsViewModel(application: Application) : AndroidViewModel(application),
    ProfileSettingsInterface {
    var profileSettings = ProfileSettingsLiveData(dbRef, sRef, aRef)
    val context: Context = application.applicationContext
    private val mMediatorLiveData = MediatorLiveData<SettingsProfileModel>()

    init {
        application.applicationContext
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

    @RequiresApi(Build.VERSION_CODES.P)
    override fun setAvatar(requestCode: Int, resultCode: Int, data: Intent?) {
        CoroutineScope(Dispatchers.IO).launch {
            val filePath: Uri
            if (requestCode == 71 && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
                filePath = data.data!!
                try {
                    val result = context.contentResolver?.let { ImageDecoder.createSource(it, filePath) }
                    val bitmap: Bitmap? = result?.let { ImageDecoder.decodeBitmap(it) }
                    if (bitmap != null) {
                        profileSettings.setAvatar(bitmap)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun setDrink(newDrink: String) {
        CoroutineScope(Dispatchers.IO).launch {
            profileSettings.setDrink(newDrink)
        }
    }

    override fun setName(newName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            profileSettings.setName(newName)
        }
    }

    override fun onAlchoo() {
        CoroutineScope(Dispatchers.IO).launch {
            profileSettings.onAlchoo()
        }
    }

    override fun offAlchoo() {
        CoroutineScope(Dispatchers.IO).launch {
            profileSettings.offAlchoo()
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
