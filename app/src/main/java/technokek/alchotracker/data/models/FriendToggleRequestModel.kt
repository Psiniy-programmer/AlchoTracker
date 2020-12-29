package technokek.alchotracker.data.models

data class FriendToggleRequestModel(
    var inFriend: Boolean,
    var requestIsSended: Boolean,
    var friendOutReq: String = "",
    var friendInReq: String = "",
    var friendList: String = ""
)
