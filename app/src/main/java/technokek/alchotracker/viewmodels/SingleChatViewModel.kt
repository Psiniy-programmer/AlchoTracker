package technokek.alchotracker.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import technokek.alchotracker.api.SingleChatInterface
import technokek.alchotracker.data.SingleChatLiveData
import technokek.alchotracker.data.models.SingleChatMessageModel

class SingleChatViewModel(application: Application, chatId: String) : AndroidViewModel(application),
    SingleChatInterface {
    var chat = SingleChatLiveData(dbRef, aRef, chatId)
    private val mMediatorLiveData = MediatorLiveData<MutableList<SingleChatMessageModel>>()

    init {
        mMediatorLiveData.addSource(chat) {
            if (it != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    mMediatorLiveData.postValue(it)
                }
            } else {
                mMediatorLiveData.value = null
            }
        }
    }

    override fun sendMessage(text: String) {
        CoroutineScope(Dispatchers.IO).launch {
            chat.sendMessage(text)
        }
    }

    companion object {
        private val dbRef = FirebaseDatabase.getInstance().getReference("chats")
        private val aRef = FirebaseAuth.getInstance()
    }
}