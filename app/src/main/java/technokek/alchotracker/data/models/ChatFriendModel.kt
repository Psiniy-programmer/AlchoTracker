package technokek.alchotracker.data.models

data class ChatFriendModel(
    var chatID: String = "",
    var name: String = "",
    var avatar: String = "",
    var lastMessage: String = "",
    var lastDateTime: String = "",
    var userID: String = "",
    var lastSenderID: String = ""
)
