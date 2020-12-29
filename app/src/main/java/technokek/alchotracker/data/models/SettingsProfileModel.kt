package technokek.alchotracker.data.models

data class SettingsProfileModel(
    val name: String = String(),
    var avatar: String = String(),
    var status: String = String(),
    var drink: String = String(),
    var alchoo: Boolean = false
)
