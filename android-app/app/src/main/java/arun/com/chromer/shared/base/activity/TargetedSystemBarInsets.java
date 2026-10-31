/*
 * Lynket Custom 2027 P15 — targeted protection for on-screen controls.
 * Used only by Intro, History and Tabs; never by floating WebHeads/dialogues.
 */
package arun.com.chromer.shared.base.activity;

import android.app.Activity;
import android.graphics.Insets;
import android.os.Build;
import android.view.View;
import android.view.WindowInsets;

import androidx.annotation.NonNull;

public final class TargetedSystemBarInsets {
    private TargetedSystemBarInsets() { }

    /**
     * On Android 15+ make the targeted screen explicitly edge-to-edge,
     * then pad its content inside the status/navigation/taskbar safe area.
     * Do not disturb the existing layouts on Android 14 or earlier.
     */
    public static void protect(@NonNull Activity activity, @NonNull View root) {
        if (Build.VERSION.SDK_INT < 35) return;

        // Explicitly choose one layout contract. If decor fitting were left
        // enabled on some devices, padding could otherwise count the bars twice.
        activity.getWindow().setDecorFitsSystemWindows(false);

        final int originalLeft = root.getPaddingLeft();
        final int originalTop = root.getPaddingTop();
        final int originalRight = root.getPaddingRight();
        final int originalBottom = root.getPaddingBottom();

        root.setOnApplyWindowInsetsListener((view, insets) -> {
            final Insets safe = insets.getInsets(
                    WindowInsets.Type.systemBars() | WindowInsets.Type.displayCutout());
            // Always calculate from original values, including after rotation.
            view.setPadding(originalLeft + safe.left, originalTop + safe.top,
                    originalRight + safe.right, originalBottom + safe.bottom);
            return insets;
        });
        root.post(root::requestApplyInsets);
    }
}
