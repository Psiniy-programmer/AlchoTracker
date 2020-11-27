package technokek.alchotracker.data.dataclasses

import android.util.Log

data class User(
    val id: Int,
    val name: String,
    val avatar: String,
    val friends: List<Int>
)
