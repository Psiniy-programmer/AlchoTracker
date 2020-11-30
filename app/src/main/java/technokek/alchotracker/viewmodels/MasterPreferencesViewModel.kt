package technokek.alchotracker.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import technokek.alchotracker.data.MasterPreferencesLiveData
import technokek.alchotracker.data.models.MasterPreferencesModel

class MasterPreferencesViewModel: ViewModel() {
    var preferences = MasterPreferencesLiveData(dbRef, aRef)
    private val mMediatorLiveData = MediatorLiveData<MutableList<MasterPreferencesModel>>()

    init {
        mMediatorLiveData.addSource(preferences) {
            if (it != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    mMediatorLiveData.postValue(it)
                }
            } else {
                mMediatorLiveData.value = null
            }
        }
    }

    fun removePreferenceItem(text: String) {
        CoroutineScope(Dispatchers.IO).launch {
            preferences.removePreferenceItem(text)
        }
    }

    fun addPreferenceItem(text: String) {
        CoroutineScope(Dispatchers.IO).launch {
            preferences.addPreferenceItem(text)
        }
    }

    companion object {
        private val dbRef = FirebaseDatabase.getInstance().getReference("users")
        private val aRef = FirebaseAuth.getInstance()
    }
}