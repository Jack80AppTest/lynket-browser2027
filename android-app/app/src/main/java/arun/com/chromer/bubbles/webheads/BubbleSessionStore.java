/*
 * Lynket 2027 — persistent open web heads (GPL-3.0-or-later).
 * The database is the source of truth; overlay views are disposable.
 */
package arun.com.chromer.bubbles.webheads;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public final class BubbleSessionStore extends SQLiteOpenHelper {
    private static final String DATABASE = "lynket_2027_active_bubbles.db";
    private static final int VERSION = 1;
    private static final String TABLE = "active_bubbles";

    public BubbleSessionStore(@NonNull Context context) {
        super(context.getApplicationContext(), DATABASE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " url TEXT NOT NULL UNIQUE, from_amp INTEGER NOT NULL DEFAULT 0,"
                + " created_at INTEGER NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // No schema migrations are necessary for the first version.
    }

    /** SQLite transaction completes before a new non-private bubble is shown. */
    public synchronized void save(@NonNull String url, boolean fromAmp) {
        ContentValues values = new ContentValues();
        values.put("url", url);
        values.put("from_amp", fromAmp ? 1 : 0);
        values.put("created_at", System.currentTimeMillis());
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.insertWithOnConflict(TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    /** Only explicit user deletion calls this method. */
    public synchronized void remove(@NonNull String url) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE, "url=?", new String[]{url});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    /** Explicit clear-all action only; never called during service destruction. */
    public synchronized void clear() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE, null, null);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    @NonNull
    public synchronized List<Entry> getAll() {
        List<Entry> entries = new ArrayList<>();
        try (Cursor cursor = getReadableDatabase().query(
                TABLE, new String[]{"url", "from_amp"}, null, null, null, null, "id ASC")) {
            while (cursor.moveToNext()) {
                entries.add(new Entry(cursor.getString(0), cursor.getInt(1) != 0));
            }
        }
        return entries;
    }

    public synchronized int count() {
        try (Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT COUNT(*) FROM " + TABLE, null)) {
            return cursor.moveToFirst() ? cursor.getInt(0) : 0;
        }
    }

    public static final class Entry {
        public final String url;
        public final boolean fromAmp;

        Entry(String url, boolean fromAmp) {
            this.url = url;
            this.fromAmp = fromAmp;
        }
    }
}
