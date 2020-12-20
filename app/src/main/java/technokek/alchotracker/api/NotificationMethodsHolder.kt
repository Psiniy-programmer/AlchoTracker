package technokek.alchotracker.api

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationManagerCompat

interface NotificationMethodsHolder {
    fun sendTimerFragmentNotificationChannel1(context: Context, notificationManager: NotificationManagerCompat,
                                              id: Int, title: String, text: String)
    fun sendTimerFragmentNotificationChannel2(context: Context, notificationManager: NotificationManagerCompat,
                                              id: Int, title: String, text: String)
}