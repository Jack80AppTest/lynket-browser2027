/*
 * Lynket — WebHeads website-list height constraint.
 *
 * The header and action buttons belong outside the scrollable list.
 * Keep their original behavior and license attribution intact.
 */
package arun.com.chromer.bubbles.webheads.ui.context;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Measures naturally for short lists, but limits the list itself so long WebHeads
 * sessions scroll without pushing Copy All, Share All or Hide Overlay off-screen.
 */
public final class CappedWebsitesRecyclerView extends RecyclerView {
    private static final int MAX_VISIBLE_ROWS = 7;
    private static final int ROW_HEIGHT_BUDGET_DP = 88;
    // Header 48dp + Copy/Share 42dp + Hide 48dp + room for dialog margins.
    private static final int RESERVED_PANEL_HEIGHT_DP = 48 + 42 + 48 + 64;

    public CappedWebsitesRecyclerView(@NonNull Context context) {
        super(context);
    }

    public CappedWebsitesRecyclerView(@NonNull Context context,
                                      @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CappedWebsitesRecyclerView(@NonNull Context context,
                                      @Nullable AttributeSet attrs,
                                      int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /** Pixel budget for the scrolling area, recalculated on every measure/rotation. */
    static int calculateMaximumHeight(int visibleHeightPx, float density) {
        int sevenRows = Math.round(MAX_VISIBLE_ROWS * ROW_HEIGHT_BUDGET_DP * density);
        int reserved = Math.round(RESERVED_PANEL_HEIGHT_DP * density);
        return Math.max(0, Math.min(sevenRows, visibleHeightPx - reserved));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Rect visibleWindow = new Rect();
        getWindowVisibleDisplayFrame(visibleWindow);
        int usableHeight = visibleWindow.height();
        if (usableHeight <= 0) {
            usableHeight = getResources().getDisplayMetrics().heightPixels;
        }

        int maxHeight = calculateMaximumHeight(
                usableHeight, getResources().getDisplayMetrics().density);

        // Respect smaller parent bounds if the dialog/window provides them.
        if (View.MeasureSpec.getMode(heightMeasureSpec) != View.MeasureSpec.UNSPECIFIED) {
            maxHeight = Math.min(maxHeight, View.MeasureSpec.getSize(heightMeasureSpec));
        }

        // AT_MOST, not EXACTLY: with 1–3 sites the dialog still shrinks to content.
        super.onMeasure(widthMeasureSpec,
                View.MeasureSpec.makeMeasureSpec(maxHeight, View.MeasureSpec.AT_MOST));
    }
}
