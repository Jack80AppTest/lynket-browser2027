/*
 * Lynket — shareable, local WebHeads URL backups.
 * Document-picker APIs are guarded so minSdk 16 remains supported.
 */
package arun.com.chromer.bubbles.webheads;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.List;

import arun.com.chromer.R;

public final class BubbleBackupCoordinator {
    private static final int REQUEST_EXPORT = 8101;
    private static final int REQUEST_IMPORT = 8102;
    private static final int MAX_CHARS = 1024 * 1024;

    private BubbleBackupCoordinator() { }

    @Nullable
    private static Activity findActivity(Context context) {
        for (int i = 0; context != null && i < 20; i++) {
            if (context instanceof Activity) return (Activity) context;
            context = context instanceof ContextWrapper
                    ? ((ContextWrapper) context).getBaseContext() : null;
        }
        return null;
    }

    public static void launchExport(View v) {
        Activity activity = findActivity(v.getContext());
        if (activity == null) return;
        try (BubbleSessionStore store = new BubbleSessionStore(activity)) {
            if (store.count() == 0) {
                show(activity, R.string.webheads_backup_no_links);
                return;
            }
            if (Build.VERSION.SDK_INT < 19) {
                // Android 4.1–4.3 have no ACTION_CREATE_DOCUMENT picker.
                // Share the portable JSON as text via an app of the user's choice.
                Intent send = new Intent(Intent.ACTION_SEND);
                send.setType("text/plain");
                send.putExtra(Intent.EXTRA_TEXT, BubbleBackupCodec.encode(store.getAll()));
                activity.startActivity(Intent.createChooser(send,
                        activity.getString(R.string.webheads_backup_legacy)));
                return;
            }
            Intent select = new Intent(Intent.ACTION_CREATE_DOCUMENT);
            select.addCategory(Intent.CATEGORY_OPENABLE);
            select.setType("application/json");
            select.putExtra(Intent.EXTRA_TITLE,
                    activity.getString(R.string.webheads_backup_filename));
            activity.startActivityForResult(select, REQUEST_EXPORT);
        } catch (RuntimeException | JSONException error) {
            show(activity, R.string.webheads_backup_failed);
        }
    }

    public static void launchImport(View v) {
        Activity activity = findActivity(v.getContext());
        if (activity == null) return;
        try {
            Intent select = new Intent(Build.VERSION.SDK_INT >= 19
                    ? Intent.ACTION_OPEN_DOCUMENT : Intent.ACTION_GET_CONTENT);
            select.addCategory(Intent.CATEGORY_OPENABLE);
            select.setType("*/*");
            activity.startActivityForResult(select, REQUEST_IMPORT);
        } catch (RuntimeException error) {
            show(activity, R.string.webheads_backup_failed);
        }
    }

    /** Returns true if the result belongs to the WebHeads backup picker. */
    public static boolean handleActivityResult(Activity activity, int request,
                                               int result, @Nullable Intent data) {
        if (request != REQUEST_EXPORT && request != REQUEST_IMPORT) return false;
        if (result != Activity.RESULT_OK || data == null || data.getData() == null) return true;
        Uri uri = data.getData();
        try {
            if (request == REQUEST_EXPORT) {
                List<BubbleSessionStore.Entry> entries;
                try (BubbleSessionStore store = new BubbleSessionStore(activity)) {
                    entries = store.getAll();
                }
                String backup = BubbleBackupCodec.encode(entries);
                try (OutputStream output = activity.getContentResolver().openOutputStream(uri)) {
                    if (output == null) throw new IOException("Output unavailable");
                    try (Writer writer = new OutputStreamWriter(output, "UTF-8")) {
                        writer.write(backup);
                    }
                }
                Toast.makeText(activity,
                        activity.getString(R.string.webheads_backup_saved, entries.size()),
                        Toast.LENGTH_LONG).show();
            } else {
                String content;
                try (InputStream input = activity.getContentResolver().openInputStream(uri)) {
                    if (input == null) throw new IOException("Input unavailable");
                    content = readLimited(input);
                }
                List<BubbleSessionStore.Entry> entries = BubbleBackupCodec.decode(content);
                if (entries.isEmpty()) {
                    show(activity, R.string.webheads_backup_invalid);
                    return true;
                }
                int added = 0;
                int existing = 0;
                try (BubbleSessionStore store = new BubbleSessionStore(activity)) {
                    // Never clear existing URLs or import duplicates.
                    for (BubbleSessionStore.Entry entry : entries) {
                        int before = store.count();
                        store.save(entry.url, entry.fromAmp);
                        if (store.count() > before) added++;
                        else existing++;
                    }
                }
                BubbleSessionRestorer.restore(activity);
                Toast.makeText(activity,
                        activity.getString(R.string.webheads_backup_imported, added, existing),
                        Toast.LENGTH_LONG).show();
            }
        } catch (IOException | JSONException | RuntimeException error) {
            show(activity, R.string.webheads_backup_failed);
        }
        return true;
    }

    private static String readLimited(InputStream input) throws IOException {
        StringBuilder result = new StringBuilder();
        try (Reader reader = new InputStreamReader(input, "UTF-8")) {
            char[] buffer = new char[4096];
            int n;
            while ((n = reader.read(buffer)) != -1) {
                if (result.length() + n > MAX_CHARS) throw new IOException("Backup too large");
                result.append(buffer, 0, n);
            }
        }
        return result.toString();
    }

    private static void show(Context context, int stringId) {
        Toast.makeText(context, stringId, Toast.LENGTH_LONG).show();
    }
}
