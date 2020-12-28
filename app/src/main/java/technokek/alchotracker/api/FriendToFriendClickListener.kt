package technokek.alchotracker.api

interface FriendToFriendClickListener {

    fun pressFriendToFriend(uid: String)

    fun pressFriendToProfile(uid: String)

    fun pressMember(uid: String)
}
