package technokek.alchotracker.data.models

import androidx.annotation.ColorRes
import java.time.LocalDateTime

/*
Дата класс, который описывает события и их место
 */
data class CalendarModel(
    val time: LocalDateTime,
    val eventPlace: Place,
    @ColorRes val color: Int,
    val avatar: String = "https://firebasestorage.googleapis.com/v0/b/alchotracker-4e1ca.appspot.com/o/UserImage.jpg?alt=media&token=23303a54-e59e-4f0c-a44c-bb9d7dffa328",
    val chatId: Int = 1,
    val id: String,
    val adminId: String,
    val ordinaryMembersIds: String = "",
    var userClicked: Boolean = false
) {
    data class Place(val name: String, val price: String, val place: String)
}
