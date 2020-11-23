package technokek.alchotracker.viewmodels

import androidx.lifecycle.ViewModel
import technokek.alchotracker.data.ProfileLiveData

class ProfileViewModel: ViewModel() {
    var mProfileLiveData = ProfileLiveData()
        private set
}