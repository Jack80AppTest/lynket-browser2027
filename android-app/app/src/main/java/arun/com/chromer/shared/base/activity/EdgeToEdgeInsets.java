/*
 * Lynket Custom 2027 — Android 16 edge-to-edge safe content insets.
 * Preserve existing screen contents while letting Android draw system bars.
 * This targeted View compatibility shim applies only on Android 15+.
 */
package arun.com.chromer.shared.base.activity;

import android.app.Activity;
import android.graphics.Insets;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;

import androidx.annotation.NonNull;

public final class EdgeToEdgeInsets {
    private EdgeToEdgeInsets() { }

    /** Called after setContentView; safe for the unchanged minSdk 16. */
    public static void apply(@NonNull Activity activity) {
        if (Build.VERSION.SDK_INT < 35) return;
        final View root = activity.findViewById(android.R.id.content);
        if (root == null) return;

        // Prevent layouts already using fitsSystemWindows from adding the same
        // system-bar space again below our content container.
        if (root instanceof ViewGroup) {
            final ViewGroup container = (ViewGroup) root;
            for (int i = 0; i < container.getChildCount(); i++) {
                container.getChildAt(i).setFitsSystemWindows(false);
            }
        }
        final int left = root.getPaddingLeft();
        final int top = root.getPaddingTop();
        final int right = root.getPaddingRight();
        final int bottom = root.getPaddingBottom();
        root.setOnApplyWindowInsetsListener((view, windowInsets) -> {
            final Insets bars = windowInsets.getInsets(
                    WindowInsets.Type.systemBars() | WindowInsets.Type.displayCutout());
            // Recalculate against initial padding; rotation and resize must
            // never accumulate padding across inset dispatches.
            view.setPadding(left + bars.left, top + bars.top,
                    right + bars.right, bottom + bars.bottom);
            return windowInsets;
        });
        root.post(root::requestApplyInsets);
    }
}
