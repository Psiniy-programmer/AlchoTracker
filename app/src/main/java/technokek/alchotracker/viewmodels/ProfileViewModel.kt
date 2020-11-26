package technokek.alchotracker.viewmodels

import androidx.lifecycle.ViewModel
import technokek.alchotracker.data.ProfileLiveData
import technokek.alchotracker.data.models.ProfileModel

class ProfileViewModel : ViewModel {
    var mProfileLiveData: ProfileLiveData
    constructor() {
        mProfileLiveData = ProfileLiveData()
    }

    constructor(uid: String) {
        mProfileLiveData = ProfileLiveData(uid)
    }
}