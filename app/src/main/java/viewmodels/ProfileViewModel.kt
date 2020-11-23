package viewmodels

import androidx.lifecycle.ViewModel
import data.ProfileLiveData

class ProfileViewModel: ViewModel() {
    var mProfileLiveData = ProfileLiveData()
        private set
}