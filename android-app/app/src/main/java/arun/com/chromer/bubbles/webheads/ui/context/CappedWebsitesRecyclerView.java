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
import android.view.ViewGroup;
import android.view.ViewParent;

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

    /**
     * The visible frame already excludes system-bar areas on older Android
     * versions. Account for actual dialog controls and view padding separately,
     * rather than guessing an additional 64dp of unavailable screen space.
     */
    static int calculateMaximumHeight(int visibleHeightPx, int fixedControlsPx,
                                      int panelChromePx, float density) {
        int sevenRows = Math.round(MAX_VISIBLE_ROWS * ROW_HEIGHT_BUDGET_DP * density);
        int freeHeight = visibleHeightPx - fixedControlsPx - panelChromePx;
        return Math.max(0, Math.min(sevenRows, freeHeight));
    }

    /**
     * Fixed sibling controls have explicit layout heights in this dialog.
     * A measured height is preferred if Android has already measured the view.
     */
    private static int measuredOrDeclaredHeight(@NonNull View sibling) {
        int measured = sibling.getMeasuredHeight();
        if (measured > 0) return measured;
        ViewGroup.LayoutParams params = sibling.getLayoutParams();
        return params != null && params.height > 0 ? params.height : 0;
    }

    private int fixedControlsHeight() {
        ViewParent parent = getParent();
        if (!(parent instanceof ViewGroup)) return 0;
        ViewGroup column = (ViewGroup) parent;
        int total = 0;
        for (int i = 0; i < column.getChildCount(); i++) {
            View sibling = column.getChildAt(i);
            if (sibling == this || sibling.getVisibility() == GONE) continue;
            total += measuredOrDeclaredHeight(sibling);
            ViewGroup.LayoutParams params = sibling.getLayoutParams();
            if (params instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams margins = (ViewGroup.MarginLayoutParams) params;
                total += margins.topMargin + margins.bottomMargin;
            }
        }
        return total;
    }

    /** Include only actual padding/margins between the list and window content. */
    private int containerChromeHeight() {
        int total = 0;
        View current = this;
        ViewParent parent = current.getParent();
        while (parent instanceof View) {
            View container = (View) parent;
            total += container.getPaddingTop() + container.getPaddingBottom();
            ViewGroup.LayoutParams params = current.getLayoutParams();
            if (params instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams margins = (ViewGroup.MarginLayoutParams) params;
                total += margins.topMargin + margins.bottomMargin;
            }
            current = container;
            parent = current.getParent();
        }
        return total;
    }

    /** Re-measure on rotation and resizing; no Android 30+ APIs are required. */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Rect visibleWindow = new Rect();
        getWindowVisibleDisplayFrame(visibleWindow);
        int usableHeight = visibleWindow.height();
        if (usableHeight <= 0) {
            usableHeight = getResources().getDisplayMetrics().heightPixels;
        }

        int maxHeight = calculateMaximumHeight(
                usableHeight, fixedControlsHeight(), containerChromeHeight(),
                getResources().getDisplayMetrics().density);

        // The Android dialog may impose an even smaller parent constraint.
        if (View.MeasureSpec.getMode(heightMeasureSpec) != View.MeasureSpec.UNSPECIFIED) {
            maxHeight = Math.min(maxHeight, View.MeasureSpec.getSize(heightMeasureSpec));
        }

        // AT_MOST, not EXACTLY: short sessions still shrink to their content.
        super.onMeasure(widthMeasureSpec,
                View.MeasureSpec.makeMeasureSpec(maxHeight, View.MeasureSpec.AT_MOST));
    }
}
