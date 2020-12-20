package technokek.alchotracker.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log


class RestartAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if ("android.intent.action.BOOT_COMPLETED" == intent!!.action) {

            // It is better to reset alarms using Background IntentService
            val i = Intent(context, BootService::class.java)
            BootService.enqueWork(context!!, i)
            //val service = context!!.startService(i)
            /*if (service == null) {
                // something really wrong here
                Log.e("RestartAlarmReceiver", "Could not start service ")
            } else {
                Log.e("RestartAlarmReceiver", "Successfully started service ")
            }*/
        } /*else {
            Log.e("RestartAlarmReceiver", "Received unexpected intent $intent")
        }*/
    }
}