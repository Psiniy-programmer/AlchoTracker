package technokek.alchotracker.api


interface AuthListener {

    fun onStarted()
    fun onSuccess()
    fun onFailure(message: String)
}
