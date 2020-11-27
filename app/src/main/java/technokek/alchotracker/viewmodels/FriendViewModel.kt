package technokek.alchotracker.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import technokek.alchotracker.data.models.EventModel
import technokek.alchotracker.data.models.FriendModel
import technokek.alchotracker.network.ApiRepo

class FriendViewModel : ViewModel() {

    private var mApiRepo: ApiRepo = ApiRepo()
    private var friends: MutableLiveData<ArrayList<FriendModel>>? = null

    init {
        if (friends == null) {
            friends = mApiRepo.getFriend()
        }
    }

    fun getFriends() : MutableLiveData<ArrayList<FriendModel>>? {
        return friends
    }
}
