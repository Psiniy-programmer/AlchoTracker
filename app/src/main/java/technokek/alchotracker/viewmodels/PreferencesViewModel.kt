package technokek.alchotracker.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import technokek.alchotracker.data.PreferencesLiveData
import technokek.alchotracker.data.models.PreferencesModel

class PreferencesViewModel: ViewModel() {
    var preferences = PreferencesLiveData(dbRef)
    private val mMediatorLiveData = MediatorLiveData<MutableList<PreferencesModel>>()

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

    companion object {
        private val dbRef = FirebaseDatabase.getInstance().getReference("users")
    }
}