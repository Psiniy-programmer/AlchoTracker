package technokek.alchotracker.api

interface RequestClickListener {

    fun accept(uid: String)

    fun deny(uid: String)
}