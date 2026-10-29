/*
 * Lynket 2027 — small home card below Recents (GPL-3.0-or-later).
 */
package arun.com.chromer.home.epoxycontroller.model;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.airbnb.epoxy.EpoxyModel;

import arun.com.chromer.R;
import arun.com.chromer.bubbles.webheads.BubbleSessionRestorer;
import arun.com.chromer.bubbles.webheads.BubbleSessionStore;

public final class SavedBubblesCardModel extends EpoxyModel<View> {
    @Override
    protected int getDefaultLayout() {
        return R.layout.layout_saved_bubbles_card;
    }

    @Override
    public void bind(@NonNull View view) {
        final TextView countView = view.findViewById(R.id.saved_bubbles_count);
        final TextView restoreButton = view.findViewById(R.id.saved_bubbles_restore);
        final int count;
        try (BubbleSessionStore store = new BubbleSessionStore(view.getContext())) {
            count = store.count();
        }
        countView.setText(view.getResources().getQuantityString(
                R.plurals.saved_bubbles_count, count, count));
        restoreButton.setEnabled(count > 0);
        restoreButton.setAlpha(count > 0 ? 1f : 0.45f);
        restoreButton.setOnClickListener(v -> BubbleSessionRestorer.restore(v.getContext()));
    }
}
