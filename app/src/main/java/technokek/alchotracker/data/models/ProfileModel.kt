package technokek.alchotracker.data.models

class ProfileModel(name: String, status: String, friendsCount: Int, eventsCount: Int, avatar: String) {
    var name = name
        private set
    var status = status
        private set
    var friendsCount = friendsCount
        private set
    var eventsCount = eventsCount
        private set
    var preferencesList: MutableSet<String> = mutableSetOf()
        private set
    var avatar = avatar
        private set
}