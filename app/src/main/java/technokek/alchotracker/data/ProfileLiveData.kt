package technokek.alchotracker.data

import androidx.lifecycle.LiveData
import technokek.alchotracker.data.models.ProfileModel

class ProfileLiveData : LiveData<ProfileModel>(){
    init {
//        TODO("Переделать на запрос данных из сети")
        value = ProfileModel("Test name", "status", 1000, 2000)
        for (i in 0..100) {
            value!!.preferencesList.add("Test drink ${i.toString()}")
        }
    }
}