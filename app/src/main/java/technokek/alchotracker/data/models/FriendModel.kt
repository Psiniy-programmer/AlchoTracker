package technokek.alchotracker.data.models

import android.graphics.Bitmap

class FriendModel(name: String) {

    var avatar: Bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        private set
    var name: String = "Friend{$name}"
        private set
}
