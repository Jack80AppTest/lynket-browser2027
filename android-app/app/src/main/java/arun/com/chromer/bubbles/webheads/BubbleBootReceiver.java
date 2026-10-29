/*
 * Lynket 2027 — recover a saved overlay after reboot or app update.
 * Do not launch a browser page or request permissions from the background.
 */
package arun.com.chromer.bubbles.webheads;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public final class BubbleBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) return;
        String action = intent.getAction();
        if (Intent.ACTION_BOOT_COMPLETED.equals(action)
                || Intent.ACTION_MY_PACKAGE_REPLACED.equals(action)) {
            try {
                BubbleSessionRestorer.restore(context);
            } catch (RuntimeException error) {
                Log.w("Lynket2027Boot", "Reboot restoration deferred", error);
            }
        }
    }
}
