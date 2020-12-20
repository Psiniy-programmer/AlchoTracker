package technokek.alchotracker.viewmodels.factories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import technokek.alchotracker.viewmodels.SingleChatViewModel

class SingleChatViewModelFactory(private val application: Application, val chatId: String)
    : ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SingleChatViewModel(application, chatId) as T
    }
}