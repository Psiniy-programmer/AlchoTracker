package technokek.alchotracker.viewmodels.factories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import technokek.alchotracker.viewmodels.FriendProfileViewModel

class FriendProfileFactory(private var application: Application, val uid: String) :
    ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FriendProfileViewModel(application, uid) as T
    }
}
