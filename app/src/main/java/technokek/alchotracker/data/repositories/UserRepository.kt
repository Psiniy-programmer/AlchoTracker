package technokek.alchotracker.data.repositories


class UserRepository (
    private val firebase: FirebaseSource
){
    fun login(email: String, password: String) = firebase.login(email, password)

    fun register(email: String, password: String,name: String) = firebase.register(email, password, name)

    fun setDefaultValue() = firebase.setDefaultValue()

    fun currentUser() = firebase.currentUser()
}
