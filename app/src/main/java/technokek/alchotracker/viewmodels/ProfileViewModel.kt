package technokek.alchotracker.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import technokek.alchotracker.data.models.Profile
import technokek.alchotracker.data.ProfileRepo

class ProfileViewModel : ViewModel {
    var mProfileLiveData: MutableLiveData<Profile>
    private val mRepo: ProfileRepo = ProfileRepo()

    constructor() {
        mProfileLiveData = mRepo.getProfile()
    }

    constructor(uid: String) {
        mProfileLiveData = mRepo.getProfile(uid)
    }
}