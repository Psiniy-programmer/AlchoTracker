package technokek.alchotracker.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import technokek.alchotracker.data.FriendPreferencesLiveData
import technokek.alchotracker.data.models.FriendPreferencesModel

class FriendPreferencesViewModel(application: Application, uid: String) : AndroidViewModel(
    application
) {
    var preferences = FriendPreferencesLiveData(dbRef, uid)
    private val mMediatorLiveData = MediatorLiveData<MutableList<FriendPreferencesModel>>()

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
