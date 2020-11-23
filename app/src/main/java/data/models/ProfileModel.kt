package data.models

class ProfileModel(name: String, status: String, friendsCount: Int, datesCount: Int) {
    var preferencesList: MutableSet<String> = mutableSetOf()
        private set
}