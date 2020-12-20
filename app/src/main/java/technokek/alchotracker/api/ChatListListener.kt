package technokek.alchotracker.api

import technokek.alchotracker.data.models.ChatFriendModel
import technokek.alchotracker.data.models.SearchFriendModel

interface ChatListListener {

    fun pressChatFriend(chatID: String, model: ChatFriendModel)
}
