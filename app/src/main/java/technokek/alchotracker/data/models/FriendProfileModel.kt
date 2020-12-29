package technokek.alchotracker.data.models

data class FriendProfileModel(
    var avatar: String = "",
    var name: String = "",
    var status: String = "",
    var friendsCount: Int = 0,
    var eventCount: Int = 0,
    var favouriteDrink: String = "",
    var chatID: String = "",
    var userID: String = ""
)
