package technokek.alchotracker.api

interface RequestClickListener {

    fun accept(uid: String, pos: Int)

    fun deny(uid: String, pos: Int)
}
