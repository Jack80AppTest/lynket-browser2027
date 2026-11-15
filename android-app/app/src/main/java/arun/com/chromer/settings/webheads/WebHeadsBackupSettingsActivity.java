/* Lynket — WebHeads backup/import Settings page; GPL-3.0-or-later. */
package arun.com.chromer.settings.webheads;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import arun.com.chromer.R;
import arun.com.chromer.bubbles.webheads.BubbleBackupCoordinator;
import arun.com.chromer.shared.base.activity.SubActivity;

public final class WebHeadsBackupSettingsActivity extends SubActivity {
    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_webheads_backup_settings);
        Toolbar bar = findViewById(R.id.toolbar);
        setSupportActionBar(bar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        boolean amoled = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean("amoled_black_theme", false);
        int text = amoled ? 0xFFF5F5F5 : 0xFF212121;
        int detail = amoled ? 0xFFBDBDBD : 0xFF616161;
        ((TextView)findViewById(R.id.webheads_backup_export_title)).setTextColor(text);
        ((TextView)findViewById(R.id.webheads_backup_import_title)).setTextColor(text);
        ((TextView)findViewById(R.id.webheads_backup_export_description)).setTextColor(detail);
        ((TextView)findViewById(R.id.webheads_backup_import_description)).setTextColor(detail);
        findViewById(R.id.webheads_backup_export_action).setOnClickListener(
                v -> BubbleBackupCoordinator.launchExport(this));
        findViewById(R.id.webheads_backup_import_action).setOnClickListener(
                v -> BubbleBackupCoordinator.launchImport(this));
    }
    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        BubbleBackupCoordinator.handleActivityResult(this, requestCode, resultCode, data);
    }
}
