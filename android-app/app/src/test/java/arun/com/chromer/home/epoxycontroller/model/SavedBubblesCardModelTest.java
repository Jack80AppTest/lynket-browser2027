package arun.com.chromer.home.epoxycontroller.model;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import arun.com.chromer.R;
import arun.com.chromer.bubbles.webheads.BubbleSessionStore;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.GraphicsMode;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
@GraphicsMode(GraphicsMode.Mode.LEGACY)
public class SavedBubblesCardModelTest {
    private Context context;
    private BubbleSessionStore store;

    @Before
    public void setup() {
        context = RuntimeEnvironment.application;
        context.deleteDatabase("lynket_2027_active_bubbles.db");
        store = new BubbleSessionStore(context);
    }

    @After
    public void cleanup() {
        store.close();
        context.deleteDatabase("lynket_2027_active_bubbles.db");
    }

    private View makeCardViews() {
        // Robolectric 4.3 cannot reliably inflate the old Material CardView on current
        // runners, so test actual button bindings using minimal real Android views.
        LinearLayout card = new LinearLayout(context);
        TextView hide = new TextView(context);
        hide.setId(R.id.saved_bubbles_hide);
        hide.setEnabled(true);
        card.addView(hide);
        TextView restore = new TextView(context);
        restore.setId(R.id.saved_bubbles_restore);
        restore.setEnabled(true);
        card.addView(restore);
        TextView count = new TextView(context);
        count.setId(R.id.saved_bubbles_count);
        card.addView(count);
        return card;
    }

    @Test
    public void hideAndRestoreButtonsArePresentAndEnabledForSavedTabs() {
        store.save("https://example.com/first", false);
        store.save("https://example.com/second", false);
        View card = makeCardViews();
        new SavedBubblesCardModel().bind(card);

        TextView hide = card.findViewById(R.id.saved_bubbles_hide);
        TextView restore = card.findViewById(R.id.saved_bubbles_restore);
        TextView counter = card.findViewById(R.id.saved_bubbles_count);

        assertNotNull(hide);
        assertNotNull(restore);
        assertTrue(hide.isEnabled());
        assertTrue(restore.isEnabled());
        assertTrue(counter.getText().toString().contains("2"));

        hide.performClick();
        // Hiding can only dismiss the visible overlay; the session is still present.
        assertEquals(2, store.count());
    }

    @Test
    public void bothActionsDisabledWhenNoSavedTabs() {
        View card = makeCardViews();
        new SavedBubblesCardModel().bind(card);
        assertFalse(card.findViewById(R.id.saved_bubbles_hide).isEnabled());
        assertFalse(card.findViewById(R.id.saved_bubbles_restore).isEnabled());
        assertEquals(0, store.count());
    }
}
