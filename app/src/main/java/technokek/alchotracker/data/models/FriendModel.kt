package technokek.alchotracker.data.models

data class FriendModel(
    var id: String = "",
    var name: String = "",
    var avatar: String = "",
    var incoming: String = "",
    var outgoing: String = "",
    var friendsCount: Int = 0,
    var friendsList: String = ""
)
