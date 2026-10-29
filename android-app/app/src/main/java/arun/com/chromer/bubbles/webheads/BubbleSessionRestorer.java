/*
 * Lynket 2027 — restoration entry point (GPL-3.0-or-later).
 * Starting the service is idempotent; WebHeadService reconciles visible bubbles.
 */
package arun.com.chromer.bubbles.webheads;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import arun.com.chromer.settings.Preferences;
import arun.com.chromer.util.Utils;

public final class BubbleSessionRestorer {
    private static final String TAG = "Lynket2027Restore";

    private BubbleSessionRestorer() {
    }

    public static void restore(@NonNull Context context) {
        // Only restore the WebHeads mode, not Android native bubbles.
        if (!Preferences.get(context).webHeads() || !Utils.isOverlayGranted(context)) {
            return;
        }
        try (BubbleSessionStore store = new BubbleSessionStore(context)) {
            if (store.count() == 0) {
                return;
            }
        } catch (RuntimeException error) {
            Log.e(TAG, "Could not read saved web heads", error);
            return;
        }

        try {
            ContextCompat.startForegroundService(context,
                    new Intent(context, WebHeadService.class));
        } catch (RuntimeException error) {
            // Android may deny background FGS launches. Data remains on disk.
            Log.w(TAG, "Overlay restoration deferred until the next allowed launch", error);
        }
    }
}
