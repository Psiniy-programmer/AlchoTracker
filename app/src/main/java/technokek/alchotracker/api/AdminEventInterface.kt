package technokek.alchotracker.api

import android.content.Intent

interface AdminEventInterface {
    fun decodeAndSetAvatar(requestCode: Int, resultCode: Int, data: Intent?)

}