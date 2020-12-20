package technokek.alchotracker.api

import android.graphics.Bitmap

interface ProfileSettingsInterface {
    fun setStatus(newStatus: String)
    fun setAvatar(newAvatar: Bitmap)
    fun setDrink(newDrink: String)
    fun onAlchoo()
    fun offAlchoo()
    fun signOut()
}