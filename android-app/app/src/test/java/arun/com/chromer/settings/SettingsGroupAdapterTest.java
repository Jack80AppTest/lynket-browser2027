package arun.com.chromer.settings;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Regression test: the phone's system night setting must never make text
 * white when Lynket's AMOLED switch is off.
 */
public class SettingsGroupAdapterTest {
    @Test
    public void lightThemeHasDarkSettingsTitles() {
        assertEquals(0xFF212121, SettingsGroupAdapter.titleColorForTheme(false));
    }

    @Test
    public void amoledThemeHasLightSettingsTitles() {
        assertEquals(0xFFF5F5F5, SettingsGroupAdapter.titleColorForTheme(true));
    }
}
