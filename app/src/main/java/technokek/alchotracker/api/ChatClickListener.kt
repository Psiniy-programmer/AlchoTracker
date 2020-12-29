package technokek.alchotracker.api

import technokek.alchotracker.data.models.FriendModel

interface ChatClickListener {

    fun pressChat(chatID: String, model: FriendModel)
}
