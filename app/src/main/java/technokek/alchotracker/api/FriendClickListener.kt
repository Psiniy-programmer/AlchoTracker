package technokek.alchotracker.api

import technokek.alchotracker.data.models.FriendModel

interface FriendClickListener {

    fun pressFriend(uid: String)

    fun pressChat(chatID: String, model: FriendModel)
}
