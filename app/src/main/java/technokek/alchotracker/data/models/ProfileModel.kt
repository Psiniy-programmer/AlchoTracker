package technokek.alchotracker.data.models

public data class ProfileModel(
    var avatar: String = "",
    var name: String = "",
    var status: String = "",
    var friendsCount: Int = 0,
    var eventCount: Int = 0,
    var favouriteDrink: String = ""
)