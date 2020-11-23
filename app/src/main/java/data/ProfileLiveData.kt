package data

import androidx.lifecycle.LiveData
import data.models.ProfileModel

class ProfileLiveData : LiveData<ProfileModel>(){
    init {
        value = ProfileModel("Test name", "status", 1000, 2000)
        for (i in 0..100) {
            value!!.preferencesList.add("Test drink ${i.toString()}")
        }
    }
}