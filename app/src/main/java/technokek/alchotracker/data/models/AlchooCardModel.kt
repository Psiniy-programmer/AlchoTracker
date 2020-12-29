package technokek.alchotracker.data.models

data class AlchooCardModel(
    val avatar: String = String(),
    val name: String = String(),
    val status: String = String(),
    var id: String = String(),
    var incomingRequests: String = String(),
    var outGoingRequests: String = String(),
    var declineList: String = String(),
    val favouriteDrink: String = String()
    )