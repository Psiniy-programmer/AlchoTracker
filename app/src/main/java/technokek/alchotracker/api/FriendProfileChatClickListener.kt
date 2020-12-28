package technokek.alchotracker.api

import technokek.alchotracker.data.models.ChatFriendModel

interface FriendProfileChatClickListener {
    fun pressChatFromFriend(chatID: String, avatar: String, name: String, uid: String)
}