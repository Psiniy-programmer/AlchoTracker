package technokek.alchotracker.api

interface FriendClickListener {

    fun pressFriend(uid: String)

    fun pressChat(chatID: String)
}
