package technokek.alchotracker.data.models

data class MasterProfileModel(
    var avatar: String = "",
    var name: String = "",
    var status: String = "",
    var friendsCount: Int = 0,
    var eventCount: Int = 0,
    var favouriteDrink: String = ""
)