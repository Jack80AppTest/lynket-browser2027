package arun.com.chromer.home.epoxycontroller.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
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

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
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

    @Test
    public void hideAndRestoreButtonsArePresentAndEnabledForSavedTabs() {
        store.save("https://example.com/first", false);
        store.save("https://example.com/second", false);
        View card = LayoutInflater.from(context).inflate(R.layout.layout_saved_bubbles_card, null);
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
        View card = LayoutInflater.from(context).inflate(R.layout.layout_saved_bubbles_card, null);
        new SavedBubblesCardModel().bind(card);
        assertFalse(card.findViewById(R.id.saved_bubbles_hide).isEnabled());
        assertFalse(card.findViewById(R.id.saved_bubbles_restore).isEnabled());
        assertEquals(0, store.count());
    }
}
