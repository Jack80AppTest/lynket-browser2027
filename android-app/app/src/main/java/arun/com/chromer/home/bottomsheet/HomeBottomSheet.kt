package arun.com.chromer.home.bottomsheet

import android.content.Intent
import android.content.Intent.*
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.preference.PreferenceManager
import arun.com.chromer.R
import arun.com.chromer.about.AboutAppActivity
import arun.com.chromer.intro.ChromerIntroActivity
import arun.com.chromer.payments.DonateActivity
import arun.com.chromer.shared.Constants
import arun.com.chromer.util.Utils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mikepenz.community_material_typeface_library.CommunityMaterial
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import kotlinx.android.synthetic.main.layout_home_bottom_sheet.*

class HomeBottomSheet : BottomSheetDialogFragment() {

    companion object {
        private const val INTRO = 1L
        private const val FEEDBACK = 2L
        private const val RATE = 3L
        private const val BETA = 4L
        private const val SHARE = 5L
        private const val DONATION = 6L
        private const val ABOUT = 7L
    }

    // AMOLED applies only to this MaterialDrawer; light mode stays unchanged.
    private fun PrimaryDrawerItem.applyAmoledColors(amoled: Boolean): PrimaryDrawerItem = apply {
        if (amoled) {
            withTextColorRes(R.color.material_dark_color)
            withDescriptionTextColorRes(R.color.material_dark_light)
            withIconColorRes(R.color.material_dark_light)
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.layout_home_bottom_sheet, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val amoledTheme = PreferenceManager.getDefaultSharedPreferences(requireContext())
                .getBoolean("amoled_black_theme", false)
        val drawer = DrawerBuilder().run {
            withActivity(requireActivity())
            if (amoledTheme) {
                withSliderBackgroundColorRes(R.color.amoled_home_surface)
            }
            withSavedInstance(savedInstanceState)
            withAccountHeader(
                    AccountHeaderBuilder().run {
                        withActivity(requireActivity())
                        withHeaderBackground(R.drawable.chromer_header_small)
                        withHeaderBackgroundScaleType(ImageView.ScaleType.CENTER_CROP)
                        withDividerBelowHeader(true)
                        build()
                    })
            addStickyDrawerItems()
            addDrawerItems(
                    PrimaryDrawerItem()
                            .withName(getString(R.string.intro))
                            .withIdentifier(INTRO)
                            .withIcon(CommunityMaterial.Icon.cmd_clipboard_text)
                            .withSelectable(false).applyAmoledColors(amoledTheme),
                    PrimaryDrawerItem()
                            .withName(getString(R.string.feedback))
                            .withIdentifier(FEEDBACK)
                            .withIcon(CommunityMaterial.Icon.cmd_message_text)
                            .withSelectable(false).applyAmoledColors(amoledTheme),
                    PrimaryDrawerItem()
                            .withName(getString(R.string.rate_play_store))
                            .withIdentifier(RATE)
                            .withIcon(CommunityMaterial.Icon.cmd_comment_text)
                            .withSelectable(false).applyAmoledColors(amoledTheme),
                    PrimaryDrawerItem()
                            .withName(R.string.join_beta)
                            .withIdentifier(BETA)
                            .withIcon(CommunityMaterial.Icon.cmd_beta)
                            .withSelectable(false).applyAmoledColors(amoledTheme),
                    DividerDrawerItem(),
                    PrimaryDrawerItem()
                            .withName(getString(R.string.share))
                            .withIcon(CommunityMaterial.Icon.cmd_share_variant)
                            .withDescription(getString(R.string.help_chromer_grow))
                            .withIdentifier(SHARE)
                            .withSelectable(false).applyAmoledColors(amoledTheme),
                    PrimaryDrawerItem()
                            .withName(getString(R.string.support_development))
                            .withDescription(R.string.consider_donation)
                            .withIcon(CommunityMaterial.Icon.cmd_heart)
                            .withIdentifier(DONATION)
                            .withSelectable(false).applyAmoledColors(amoledTheme)
                            .withIconColorRes(R.color.accent),
                    PrimaryDrawerItem()
                            .withName(getString(R.string.about))
                            .withIcon(CommunityMaterial.Icon.cmd_information)
                            .withIdentifier(ABOUT)
                            .withSelectable(false).applyAmoledColors(amoledTheme)
            )
            withOnDrawerItemClickListener { _, _, drawerItem ->
                when (drawerItem.identifier) {
                    FEEDBACK -> {
                        val emailIntent = Intent(ACTION_SENDTO, Uri.fromParts("mailto", Constants.MAILID, null))
                        emailIntent.putExtra(EXTRA_SUBJECT, getString(R.string.app_name))
                        startActivity(createChooser(emailIntent, getString(R.string.send_email)))
                    }
                    BETA -> startActivity(Intent(ACTION_VIEW, "https://play.google.com/apps/testing/arun.com.chromer".toUri()))
                    RATE -> Utils.openPlayStore(requireActivity(), requireActivity().packageName)
                    INTRO -> startActivity(Intent(requireActivity(), ChromerIntroActivity::class.java))
                    DONATION -> startActivity(Intent(requireActivity(), DonateActivity::class.java))
                    SHARE -> {
                        val shareIntent = Intent(ACTION_SEND)
                        shareIntent.putExtra(EXTRA_TEXT, getString(R.string.share_text))
                        shareIntent.type = "text/plain"
                        startActivity(createChooser(shareIntent, getString(R.string.share_via)))
                    }
                    ABOUT -> startActivity(Intent(requireActivity(), AboutAppActivity::class.java))
                }
                false
            }
            withDelayDrawerClickEvent(200)
            buildView()
        }.apply {
            setSelection(-1)
        }
        homeBottomSheet.addView(drawer.slider)
    }
}