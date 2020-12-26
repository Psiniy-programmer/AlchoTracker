package technokek.alchotracker.fcm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import technokek.alchotracker.R
import kotlin.random.Random

class ChatMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)

        Log.d("MSG", p0.notification?.body.toString())
        p0.notification?.let { showNotification(it) }
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        Log.d("NEW_TOKEN", p0)
    }

    private fun showNotification(message: RemoteMessage.Notification) {
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel: NotificationChannel =
                NotificationChannel(
                    NOTIFICATION_CHANNEL_ID, "New Message",
                    NotificationManager.IMPORTANCE_DEFAULT
                )

            notificationChannel.description = "Techrush Channel"
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.BLUE
            notificationChannel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)

            notificationManager.createNotificationChannel(notificationChannel)

            val notificationBuilder: NotificationCompat.Builder =
                NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_emoji_people_black_24dp)
                .setContentTitle(message.title)
                .setContentText(message.body)
                .setContentInfo("Info")

            notificationManager.notify(
                Random(1).nextInt(),
                notificationBuilder.build()
            )
        }
    }

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "technokek.alchotracker"
    }
}
