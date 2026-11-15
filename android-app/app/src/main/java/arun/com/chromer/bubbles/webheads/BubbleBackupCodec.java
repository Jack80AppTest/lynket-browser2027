/*
 * Lynket WebHeads portable URL backup. Never exports browser history,
 * cookies, permissions or private/incognito pages.
 */
package arun.com.chromer.bubbles.webheads;

import android.net.Uri;
import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class BubbleBackupCodec {
    public static final int MAX_LINKS = 500;
    private static final int MAX_URL_CHARS = 8192;
    private BubbleBackupCodec() { }

    @NonNull
    public static String encode(@NonNull List<BubbleSessionStore.Entry> entries)
            throws JSONException {
        JSONObject root = new JSONObject();
        root.put("format", "lynket-webheads");
        root.put("version", 1);
        JSONArray urls = new JSONArray();
        for (BubbleSessionStore.Entry entry : entries) {
            if (!validUrl(entry.url)) continue;
            if (urls.length() >= MAX_LINKS) break;
            JSONObject item = new JSONObject();
            item.put("url", entry.url);
            item.put("from_amp", entry.fromAmp);
            urls.put(item);
        }
        root.put("webheads", urls);
        return root.toString(2);
    }

    /** Reads this app's JSON backup as well as its existing comma/newline text exports. */
    @NonNull
    public static List<BubbleSessionStore.Entry> decode(@NonNull String input)
            throws JSONException {
        if (input.length() > 1024 * 1024) throw new JSONException("Backup too large");
        LinkedHashMap<String, BubbleSessionStore.Entry> ordered = new LinkedHashMap<>();
        String body = input.trim();
        if (body.startsWith("{")) {
            JSONObject root = new JSONObject(body);
            if (!"lynket-webheads".equals(root.optString("format"))
                    || root.optInt("version") != 1) {
                throw new JSONException("Unsupported Lynket backup format");
            }
            JSONArray items = root.getJSONArray("webheads");
            if (items.length() > MAX_LINKS) throw new JSONException("Too many links");
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                add(ordered, item.optString("url"), item.optBoolean("from_amp", false));
            }
        } else {
            // A literal comma inside a URL is preserved unless it is followed
            // by another http(s) URL, as in Lynket's "Comma separated" export.
            for (String line : body.split("\\r?\\n|,\\s+(?=https?://)")) {
                add(ordered, line.trim(), false);
            }
        }
        return new ArrayList<>(ordered.values());
    }

    private static void add(Map<String, BubbleSessionStore.Entry> output,
                            String url, boolean amp) {
        if (output.size() < MAX_LINKS && validUrl(url) && !output.containsKey(url)) {
            output.put(url, new BubbleSessionStore.Entry(url, amp));
        }
    }

    private static boolean validUrl(String url) {
        if (url == null || url.length() == 0 || url.length() > MAX_URL_CHARS) return false;
        if (url.indexOf(' ') >= 0 || url.indexOf('\n') >= 0 || url.indexOf('\r') >= 0) return false;
        try {
            Uri parsed = Uri.parse(url);
            String scheme = parsed.getScheme();
            return ("http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme))
                    && parsed.getHost() != null && parsed.getHost().length() > 0;
        } catch (RuntimeException error) {
            return false;
        }
    }
}
