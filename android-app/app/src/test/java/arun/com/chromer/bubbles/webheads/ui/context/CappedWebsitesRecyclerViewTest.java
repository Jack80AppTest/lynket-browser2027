package arun.com.chromer.bubbles.webheads.ui.context;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Screens of different heights: the actual header/footer/padding are reserved.
 * A 7-row cap still applies, but 8 or 20 sites scroll within the same viewport.
 */
public class CappedWebsitesRecyclerViewTest {
    private static final int ROW_DP = 88;
    private static final int FIXED_ACTIONS_DP = 48 + 42 + 48;

    @Test
    public void portraitKeepsNaturalShortListsAndCapsEightOrTwenty() {
        int height = CappedWebsitesRecyclerView.calculateMaximumHeight(
                950, FIXED_ACTIONS_DP, 12, 1f);
        assertEquals(7 * ROW_DP, height);
        assertEquals(3 * ROW_DP, Math.min(3 * ROW_DP, height));
        assertEquals(7 * ROW_DP, Math.min(7 * ROW_DP, height));
        assertTrue(8 * ROW_DP > height);
        assertTrue(20 * ROW_DP > height);
    }

    @Test
    public void landscapeReservesMeasuredHeaderActionsAndPaddingOnly() {
        int height = CappedWebsitesRecyclerView.calculateMaximumHeight(
                420, FIXED_ACTIONS_DP, 12, 1f);
        assertEquals(420 - FIXED_ACTIONS_DP - 12, height);
        assertTrue("Three normal rows can fit", height >= 3 * ROW_DP);
        assertTrue(7 * ROW_DP > height);
        assertTrue(8 * ROW_DP > height);
        assertTrue(20 * ROW_DP > height);
    }

    @Test
    public void actualMarginsAreSubtractedAndDensityIsRespected() {
        assertEquals(2 * 7 * ROW_DP,
                CappedWebsitesRecyclerView.calculateMaximumHeight(1900,
                        FIXED_ACTIONS_DP * 2, 12 * 2, 2f));
        assertEquals(222, CappedWebsitesRecyclerView.calculateMaximumHeight(
                390, FIXED_ACTIONS_DP, 30, 1f));
    }

    @Test
    public void tinyViewportNeverCreatesNegativeListHeight() {
        assertEquals(0, CappedWebsitesRecyclerView.calculateMaximumHeight(
                120, FIXED_ACTIONS_DP, 12, 1f));
        assertEquals(0, CappedWebsitesRecyclerView.calculateMaximumHeight(
                0, FIXED_ACTIONS_DP * 3, 36, 3f));
    }
}
