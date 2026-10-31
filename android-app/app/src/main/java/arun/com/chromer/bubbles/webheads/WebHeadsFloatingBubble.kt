package arun.com.chromer.bubbles.webheads

import android.app.Application
import android.app.ForegroundServiceStartNotAllowedException
import android.os.Build
import android.util.Log
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import androidx.core.content.ContextCompat
import arun.com.chromer.bubbles.FloatingBubble
import arun.com.chromer.data.website.model.Website
import arun.com.chromer.shared.Constants.*
import arun.com.chromer.util.Utils
import dev.arunkumar.android.rxschedulers.SchedulerProvider
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class WebHeadsFloatingBubble
@Inject
constructor(
        private val application: Application,
        private val schedulerProvider: SchedulerProvider
) : FloatingBubble {

    override fun openBubble(
            website: Website,
            fromMinimize: Boolean,
            fromAmp: Boolean,
            incognito: Boolean,
            context: Context?,
            color: Int
    ) {
        (context ?: application).let { ctx ->
            if (Utils.isOverlayGranted(ctx)) {
                val webHeadLauncher = Intent(ctx, WebHeadService::class.java).apply {
                    data = website.preferredUri()
                    addFlags(FLAG_ACTIVITY_NEW_TASK)
                    putExtra(EXTRA_KEY_MINIMIZE, fromMinimize)
                    putExtra(EXTRA_KEY_FROM_AMP, fromAmp)
                    putExtra(EXTRA_KEY_INCOGNITO, incognito)
                }
                try {
                    ContextCompat.startForegroundService(ctx, webHeadLauncher)
                } catch (error: RuntimeException) {
                    // Android 15 can refuse background foreground-service launches
                    // without a visible overlay. Keep normal URLs for next launch.
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.VANILLA_ICE_CREAM ||
                            error !is ForegroundServiceStartNotAllowedException) {
                        throw error
                    }
                    if (!incognito) {
                        val url = webHeadLauncher.dataString
                        if (!url.isNullOrEmpty()) {
                            try {
                                BubbleSessionStore(ctx).use { store -> store.save(url, fromAmp) }
                            } catch (saveError: RuntimeException) {
                                Log.e("Lynket2027WebHeads", "Could not save deferred bubble", saveError)
                            }
                        }
                    }
                    Log.w("Lynket2027WebHeads",
                            "Android 15 deferred a WebHead; saved URLs remain available", error)
                }
            } else {
                schedulerProvider.ui.scheduleDirect {
                    Utils.openDrawOverlaySettings(ctx)
                }
            }
        }
    }
}

