package technokek.alchotracker.viewmodels

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import technokek.alchotracker.data.ChatFriendLiveData
import technokek.alchotracker.data.ChatListFriendLiveData
import technokek.alchotracker.data.models.ChatFriendModel
import technokek.alchotracker.data.models.SearchFriendModel

class ChatViewModel : ViewModel() {

    private val chatListLiveData = ChatListFriendLiveData(HOT_STOCK_REF)
    private val chatFriendLiveData = ChatFriendLiveData(HOT_STOCK_CHAT)
    var mediatorChatListLiveData = MediatorLiveData<HashMap<String, MutableList<SearchFriendModel>>>()
        private set
    var mediatorChatLiveData = MediatorLiveData<MutableList<ChatFriendModel>>()
        private set

    init {
        mediatorChatListLiveData.addSource(chatListLiveData) {
            if (it != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    mediatorChatListLiveData.postValue(it)
                }
            } else {
                mediatorChatListLiveData.value = null
            }
        }
        mediatorChatLiveData.addSource(chatFriendLiveData) {
            if (it != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    mediatorChatLiveData.postValue(it)
                }
            } else {
                mediatorChatLiveData.value = null
            }
        }
    }

    fun refresh(chatID: HashMap<String, MutableList<SearchFriendModel>>) {
        chatFriendLiveData.refreshChatID(chatID)
    }

    companion object {
        private val HOT_STOCK_REF = FirebaseDatabase
            .getInstance()
            .getReference("users")

        private val HOT_STOCK_CHAT = FirebaseDatabase
            .getInstance()
            .getReference("chats")
    }
}