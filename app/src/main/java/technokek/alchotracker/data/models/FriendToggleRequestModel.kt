package technokek.alchotracker.data.models

data class FriendToggleRequestModel(
    var inFriend: Boolean,
    var requestIsSended: Boolean,
    var masterOutReq: String = "",
    var masterInReq: String = "",
    var friendOutReq: String = "",
    var friendInReq: String = "",
    var masterList: String = "",
    var friendList: String = ""
)