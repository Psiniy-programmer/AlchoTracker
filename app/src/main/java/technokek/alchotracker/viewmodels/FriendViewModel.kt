package technokek.alchotracker.viewmodels

import androidx.lifecycle.ViewModel
import technokek.alchotracker.data.FriendLiveData

class FriendViewModel : ViewModel() {

    var mFriendLiveData = FriendLiveData()
        private set
}
