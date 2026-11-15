package arun.com.chromer.settings;
import android.content.Context;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class WebHeadsBackupSettingsEntryTest {
    @Test public void backupIsFourthSettingsCategory() {
        Context context = RuntimeEnvironment.application;
        SettingsGroupAdapter adapter = new SettingsGroupAdapter(context);
        assertEquals(4, adapter.getItemCount());
        assertEquals((long) context.getString(arun.com.chromer.R.string.settings_webheads_backup).hashCode(),
                adapter.getItemId(3));
        adapter.cleanUp();
    }
}
