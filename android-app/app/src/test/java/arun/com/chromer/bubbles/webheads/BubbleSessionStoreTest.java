package arun.com.chromer.bubbles.webheads;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class BubbleSessionStoreTest {
    private Context context;
    private BubbleSessionStore store;

    @Before
    public void setUp() {
        context = RuntimeEnvironment.application;
        context.deleteDatabase("lynket_2027_active_bubbles.db");
        store = new BubbleSessionStore(context);
    }

    @After
    public void tearDown() {
        store.close();
        context.deleteDatabase("lynket_2027_active_bubbles.db");
    }

    @Test
    public void savesAndRestoresInOriginalOrderAfterClosingStore() {
        store.save("https://example.com/a", false);
        store.save("https://example.com/b", true);
        store.close();
        store = new BubbleSessionStore(context);
        List<BubbleSessionStore.Entry> entries = store.getAll();
        assertEquals(2, entries.size());
        assertEquals("https://example.com/a", entries.get(0).url);
        assertEquals("https://example.com/b", entries.get(1).url);
        assertTrue(entries.get(1).fromAmp);
    }

    @Test
    public void duplicateUrlsNeverCreateAdditionalBubbles() {
        store.save("https://example.com/a", false);
        store.save("https://example.com/a", true);
        assertEquals(1, store.count());
        assertFalse(store.getAll().get(0).fromAmp);
    }

    @Test
    public void onlyExplicitRemovalChangesPersistentSession() {
        store.save("https://example.com/a", false);
        store.save("https://example.com/b", false);
        store.close(); // Simulate service destruction/hiding.
        store = new BubbleSessionStore(context);
        assertEquals(2, store.count());
        store.remove("https://example.com/a"); // User clicked X.
        assertEquals(1, store.count());
        assertEquals("https://example.com/b", store.getAll().get(0).url);
    }

    @Test
    public void explicitClearAllDoesNotRestoreOldBubbles() {
        store.save("https://example.com/a", false);
        store.clear();
        store.close();
        store = new BubbleSessionStore(context);
        assertEquals(0, store.count());
    }
}
