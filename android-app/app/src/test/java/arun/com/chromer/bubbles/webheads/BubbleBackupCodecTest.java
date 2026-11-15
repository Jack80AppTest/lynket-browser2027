package arun.com.chromer.bubbles.webheads;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class BubbleBackupCodecTest {
    @Test
    public void jsonRoundTripPreservesOrderUrlsAndAmp() throws Exception {
        String input = BubbleBackupCodec.encode(Arrays.asList(
                new BubbleSessionStore.Entry("https://example.com/a?x=1&y=2", false),
                new BubbleSessionStore.Entry("https://share.google/abc", true)));
        List<BubbleSessionStore.Entry> restored = BubbleBackupCodec.decode(input);
        assertEquals(2, restored.size());
        assertEquals("https://example.com/a?x=1&y=2", restored.get(0).url);
        assertEquals("https://share.google/abc", restored.get(1).url);
        assertTrue(restored.get(1).fromAmp);
    }

    @Test
    public void textImportsNewlineAndCommaSeparatedWithNoDuplicates() throws Exception {
        String links = "https://example.com/a?x=1&y=2, https://site.test/one\n"
                + "https://example.com/a?x=1&y=2\nhttps://site.test/last";
        List<BubbleSessionStore.Entry> result = BubbleBackupCodec.decode(links);
        assertEquals(3, result.size());
        assertEquals("https://example.com/a?x=1&y=2", result.get(0).url);
        assertEquals("https://site.test/last", result.get(2).url);
    }

    @Test
    public void unsafeSchemesAreNotImported() throws Exception {
        List<BubbleSessionStore.Entry> result = BubbleBackupCodec.decode(
                "file:///data/private\njavascript:alert(1)\nhttps://example.com/ok");
        assertEquals(1, result.size());
        assertEquals("https://example.com/ok", result.get(0).url);
    }

    @Test(expected = org.json.JSONException.class)
    public void rejectsUnrecognizedJsonFiles() throws Exception {
        BubbleBackupCodec.decode("{\"notLynket\": true}");
    }
}
