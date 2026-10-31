/*
 * Lynket
 *
 * Copyright (C) 2019 Arunkumar
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package arun.com.chromer.browsing.webview

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.InflateException
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.core.content.ContextCompat
import arun.com.chromer.R
import arun.com.chromer.databinding.ActivityWebViewBinding
import arun.com.chromer.databinding.ActivityWebViewContentBinding
import android.view.ViewGroup
import arun.com.chromer.browsing.BrowsingActivity
import arun.com.chromer.browsing.EXTRA_CURRENT_LOADING_URL
import arun.com.chromer.browsing.menu.MenuDelegate
import arun.com.chromer.data.website.model.Website
import arun.com.chromer.di.activity.ActivityComponent
import arun.com.chromer.extenstions.applyColor
import arun.com.chromer.extenstions.setAutoHideProgress
import arun.com.chromer.shared.Constants
import arun.com.chromer.util.ColorUtil
import arun.com.chromer.util.Utils
import timber.log.Timber
import javax.inject.Inject

open class WebViewActivity : BrowsingActivity() {
    @Inject
    lateinit var menuDelegate: MenuDelegate

    private var themeColor = 0
    private var fgColorStateList: ColorStateList = ColorStateList.valueOf(0)
    private var foregroundColor = 0

    override fun inject(activityComponent: ActivityComponent) = activityComponent.inject(this)

    private val views by lazy(LazyThreadSafetyMode.NONE) {
        ActivityWebViewBinding.bind(findViewById<ViewGroup>(android.R.id.content).getChildAt(0))
    }
    private val contentViews by lazy(LazyThreadSafetyMode.NONE) {
        ActivityWebViewContentBinding.bind(findViewById(R.id.swipeRefreshLayout))
    }

    override fun getLayoutRes(): Int = R.layout.activity_web_view

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()
        setupSwipeRefresh()
        setupWebView(savedInstanceState)
        setupBottomBar()
    }

    override fun getCurrentUrl(): String = contentViews.webView.url ?: super.getCurrentUrl()

    override fun onCreateOptionsMenu(menu: Menu) = menuDelegate.createOptionsMenu(menu)

    override fun onPrepareOptionsMenu(menu: Menu) = menuDelegate.prepareOptionsMenu(menu)

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return menuDelegate.handleItemSelected(item.itemId)
    }

    private fun setupBottomBar() {
        menuDelegate.setupBottombar(views.bottomNavigation)
    }

    override fun onDestroy() {
        contentViews.webView.destroy()
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (contentViews.webView.canGoBack()) {
            contentViews.webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onWebsiteLoaded(website: Website) {
        val title = website.safeLabel()
        setToolbarTitle(title)
        setToolbarSubtitle(website.url)
    }

    override fun onToolbarColorSet(websiteThemeColor: Int) {
        super.onToolbarColorSet(websiteThemeColor)
        setAppBarColor(websiteThemeColor)
    }

    private fun setupSwipeRefresh() {
        with(contentViews.swipeRefreshLayout) {
            setOnRefreshListener {
                contentViews.webView.reload()
            }
            setColorSchemeColors(
                    ContextCompat.getColor(context, R.color.primary),
                    ContextCompat.getColor(context, R.color.accent))
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(views.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.article_ic_close)
            title = website?.safeLabel() ?: intent.dataString
        }
        val toolbarColor = intent.getIntExtra(Constants.EXTRA_KEY_TOOLBAR_COLOR, 0)
        setAppBarColor(toolbarColor)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView(savedInstanceState: Bundle?) {
        try {
            contentViews.webView.apply {
                webViewClient = object : WebViewClient() {
                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        super.onPageStarted(view, url, favicon)
                        url?.let {
                            setToolbarSubtitle(url)
                            loadWebsiteDetails(url)
                        }
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        hideLoading()
                    }
                }
                webChromeClient = object : WebChromeClient() {
                    override fun onReceivedTitle(view: WebView?, title: String?) {
                        super.onReceivedTitle(view, title)
                        setToolbarTitle(title)
                    }

                    override fun onProgressChanged(view: WebView?, newProgress: Int) {
                        super.onProgressChanged(view, newProgress)
                        setLoadingProgress(newProgress)
                    }
                }
                settings.javaScriptEnabled = true
                val urlToLoad = savedInstanceState?.getString(EXTRA_CURRENT_LOADING_URL)
                        ?: intent.dataString
                if (urlToLoad != null) {
                    loadUrl(urlToLoad)
                }
            }

        } catch (e: InflateException) {
            Timber.e(e)
            Toast.makeText(this, R.string.web_view_not_found, Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun setToolbarTitle(title: String?) {
        if (!TextUtils.isEmpty(title)) {
            views.toolbar.title = title
        }
    }

    private fun setToolbarSubtitle(subtitle: String?) {
        if (!TextUtils.isEmpty(subtitle) && views.toolbar.title != subtitle) {
            views.toolbar.subtitle = subtitle
        }
    }

    private fun setAppBarColor(themeColor: Int) {
        this.themeColor = themeColor
        foregroundColor = ColorUtil.getForegroundWhiteOrBlack(themeColor)
        fgColorStateList = ColorStateList.valueOf(foregroundColor)

        views.toolbar.apply {
            setBackgroundColor(themeColor)
            setTitleTextColor(foregroundColor)
            setSubtitleTextColor(foregroundColor)
            post {
                navigationIcon = navigationIcon!!.applyColor(foregroundColor)
                overflowIcon = overflowIcon!!.applyColor(foregroundColor)
            }
        }

        views.progressBar.apply {
            useIntrinsicPadding = false
            progressBackgroundTintList = ColorStateList.valueOf(themeColor)
            progressTintList = ColorStateList.valueOf(foregroundColor)
        }

        views.bottomNavigation.background = ColorDrawable(themeColor)
        views.bottomNavigation.itemIconTintList = ColorStateList.valueOf(foregroundColor)
        views.bottomNavigation.itemTextColor = ColorStateList.valueOf(foregroundColor)

        contentViews.swipeRefreshLayout.setColorSchemeColors(themeColor, ColorUtil.getClosestAccentColor(themeColor))
        if (Utils.ANDROID_LOLLIPOP) {
            window.statusBarColor = ColorUtil.getDarkenedColorForStatusBar(themeColor)
        }
    }

    private fun setLoadingProgress(newProgress: Int) {
        views.progressBar.setAutoHideProgress(newProgress, fgColorStateList)
    }


    private fun showLoading() {
        contentViews.swipeRefreshLayout.isRefreshing = true
    }


    private fun hideLoading() {
        contentViews.swipeRefreshLayout.isRefreshing = false
    }
}
