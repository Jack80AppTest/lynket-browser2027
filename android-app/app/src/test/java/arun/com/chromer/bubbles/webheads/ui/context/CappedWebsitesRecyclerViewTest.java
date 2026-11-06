package arun.com.chromer.bubbles.webheads.ui.context;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Regression checks for 3, 7, 8 and 20 websites and for a short landscape
 * viewport. Item heights can vary with text/font size; the height cap always
 * protects the action row independently of the number of items.
 */
public class CappedWebsitesRecyclerViewTest {
    private static final int ROW_HEIGHT_DP = 88;

    @Test
    public void portraitListGrowsForThreeAndSevenButCapsEightAndTwenty() {
        int height = CappedWebsitesRecyclerView.calculateMaximumHeight(950, 1f);
        assertEquals(7 * ROW_HEIGHT_DP, height);

        assertEquals(3 * ROW_HEIGHT_DP, Math.min(3 * ROW_HEIGHT_DP, height));
        assertEquals(7 * ROW_HEIGHT_DP, Math.min(7 * ROW_HEIGHT_DP, height));
        assertTrue(8 * ROW_HEIGHT_DP > height);
        assertTrue(20 * ROW_HEIGHT_DP > height);
    }

    @Test
    public void landscapeLeavesRoomForAllThreeFooterActions() {
        int height = CappedWebsitesRecyclerView.calculateMaximumHeight(360, 1f);
        assertEquals(360 - (48 + 42 + 48 + 64), height);
        assertTrue(height < 3 * ROW_HEIGHT_DP);
        assertTrue(height < 7 * ROW_HEIGHT_DP);
        assertTrue(height < 8 * ROW_HEIGHT_DP);
        assertTrue(height < 20 * ROW_HEIGHT_DP);
    }

    @Test
    public void capsScaleWithDensityAndNeverGoNegative() {
        assertEquals(2 * 7 * ROW_HEIGHT_DP,
                CappedWebsitesRecyclerView.calculateMaximumHeight(1900, 2f));
        assertEquals(0, CappedWebsitesRecyclerView.calculateMaximumHeight(120, 1f));
        assertEquals(0, CappedWebsitesRecyclerView.calculateMaximumHeight(0, 3f));
    }
}
