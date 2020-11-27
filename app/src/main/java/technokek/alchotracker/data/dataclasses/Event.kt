package technokek.alchotracker.data.dataclasses

import kotlinx.android.parcel.Parcelize

//@Parcelize
data class Event(
    val id: Int,
    val name: String,
    val avatar: String
)
