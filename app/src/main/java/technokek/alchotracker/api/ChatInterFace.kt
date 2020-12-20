package technokek.alchotracker.api

import technokek.alchotracker.data.models.FriendModel

interface ChatInterFace {

    fun createChat(model: FriendModel)
}