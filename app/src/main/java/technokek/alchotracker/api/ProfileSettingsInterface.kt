package technokek.alchotracker.api

import android.net.Uri

interface ProfileSettingsInterface {
    fun setStatus(newStatus: String)
    fun setAvatar(newAvatar: Uri)
    fun setDrink(newDrink: String)
    fun signOut()
}