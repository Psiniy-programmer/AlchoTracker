package technokek.alchotracker.data.models

import com.google.firebase.database.*
import technokek.alchotracker.data.dataclasses.Event


data class EventModel(
    val id: Int,
    val name: String,
    val avatar: String
)
