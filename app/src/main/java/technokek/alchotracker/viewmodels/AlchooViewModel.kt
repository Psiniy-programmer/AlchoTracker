package technokek.alchotracker.viewmodels

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import technokek.alchotracker.data.AlchooLiveData
import technokek.alchotracker.data.models.AlchooCardModel

class AlchooViewModel : ViewModel() {
    var alchoo = AlchooLiveData(dbRef, aRef)
    private val mMediatorLiveData = MediatorLiveData<MutableList<AlchooCardModel>>()

    init {
        mMediatorLiveData.addSource(alchoo) {
            if (it != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    mMediatorLiveData.postValue(it)
                    Log.d("SYKA", "TEST")
                }
            } else {
                mMediatorLiveData.value = null
            }
        }
    }

    companion object {
        private val dbRef = FirebaseDatabase.getInstance().getReference("users")
        private val aRef = FirebaseAuth.getInstance()
    }
}