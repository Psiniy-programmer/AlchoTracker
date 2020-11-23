package technokek.alchotracker.data.models

class ProfileModel(name: String, status: String, friendsCount: Int, datesCount: Int) {
    var name = name
        private set
    var status = status
        private set
    var friendsCount = friendsCount
        private set
    var datesCount = datesCount
        private set
    var preferencesList: MutableSet<String> = mutableSetOf()
        private set
}