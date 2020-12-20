package technokek.alchotracker.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import technokek.alchotracker.api.AlchooInterface
import technokek.alchotracker.api.AlchooStatusInterface
import technokek.alchotracker.data.AlchooLiveData
import technokek.alchotracker.data.AlchooStatusLiveData
import technokek.alchotracker.data.models.AlchooCardModel
import technokek.alchotracker.data.models.AlchooStatusModel

class AlchooViewModel : ViewModel(), AlchooInterface, AlchooStatusInterface {
    var alchoo = AlchooLiveData(dbRef, aRef)
    var status = AlchooStatusLiveData(dbRef, aRef)
    private val mMediatorLiveData = MediatorLiveData<MutableList<AlchooCardModel>>()
    private val mStatusMediatorLiveData = MediatorLiveData<AlchooStatusModel>()

    init {
        mMediatorLiveData.addSource(alchoo) {
            if (it != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    mMediatorLiveData.postValue(it)
                }
            } else {
                mMediatorLiveData.value = null
            }
        }
        mStatusMediatorLiveData.addSource(status) {
            if (it != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    mStatusMediatorLiveData.postValue(it)
                }
            } else {
                mStatusMediatorLiveData.value = null
            }
        }
    }

    override fun acceptBody(uid: String) {
        CoroutineScope(Dispatchers.IO).launch {
            alchoo.acceptBody(uid)
        }
    }

    override fun declineBody(uid: String) {
        CoroutineScope(Dispatchers.IO).launch {
            alchoo.declineBody(uid)
        }
    }

    override fun refreshList() {
        CoroutineScope(Dispatchers.IO).launch {
            alchoo.refreshList()
        }
    }

    override fun changeStatus() {
        CoroutineScope(Dispatchers.IO).launch {
            status.changeStatus()
        }
    }

    companion object {
        private val dbRef = FirebaseDatabase.getInstance().getReference("users")
        private val aRef = FirebaseAuth.getInstance()
    }
}