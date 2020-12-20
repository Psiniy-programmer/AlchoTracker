package technokek.alchotracker.api

interface AlchooInterface {
    fun acceptBody(uid: String)
    fun declineBody(uid: String)
    fun refreshList()
}