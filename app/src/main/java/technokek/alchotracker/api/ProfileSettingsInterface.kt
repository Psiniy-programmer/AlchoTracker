package technokek.alchotracker.api

import android.content.Intent

interface ProfileSettingsInterface {
    fun setStatus(newStatus: String)
    fun setAvatar(requestCode: Int, resultCode: Int, data: Intent?)
    fun setDrink(newDrink: String)
    fun onAlchoo()
    fun offAlchoo()
    fun signOut()
}