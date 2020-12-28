package technokek.alchotracker.data.repositories

import technokek.alchotracker.api.SignUpInterface


class UserRepository(
    private val firebase: FirebaseSource
) {
    fun login(email: String, password: String) = firebase.login(email, password)

    fun register(email: String, password: String, name: String, callback: SignUpInterface) =
        firebase.register(email, password, name, callback)

    fun currentUser() = firebase.currentUser()
}
