package technokek.alchotracker.data.models

import android.graphics.Bitmap

class EventModel(name: String) {

    var avatar: Bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        private set
    var name: String = "Event{$name}"
        private set
}
