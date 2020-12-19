package technokek.alchotracker.viewmodels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import technokek.alchotracker.api.AlchooInterface
import technokek.alchotracker.data.AlchooLiveData
import technokek.alchotracker.data.models.AlchooCardModel

class AlchooViewModel : ViewModel(), AlchooInterface {
    var alchoo = AlchooLiveData(dbRef, aRef)
    private val mMediatorLiveData = MediatorLiveData<MutableList<AlchooCardModel>>()

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

    companion object {
        private val dbRef = FirebaseDatabase.getInstance().getReference("users")
        private val aRef = FirebaseAuth.getInstance()
    }
}