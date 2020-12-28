package technokek.alchotracker.api

import technokek.alchotracker.data.models.FriendModel

interface FriendClickListener {

    fun pressFriend(uid: String)
}
