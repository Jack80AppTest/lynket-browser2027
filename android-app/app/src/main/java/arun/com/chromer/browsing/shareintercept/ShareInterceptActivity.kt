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

package arun.com.chromer.browsing.shareintercept

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Intent.*
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import arun.com.chromer.R
import arun.com.chromer.data.website.model.Website
import arun.com.chromer.di.activity.ActivityComponent
import arun.com.chromer.search.provider.SearchProviders
import arun.com.chromer.shared.base.activity.BaseActivity
import arun.com.chromer.tabs.TabsManager
import arun.com.chromer.util.SafeIntent
import arun.com.chromer.util.Utils
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber
import javax.inject.Inject

@SuppressLint("GoogleAppIndexingApiWarning")
class ShareInterceptActivity : BaseActivity() {

    override fun getLayoutRes() = 0

    @Inject
    lateinit var tabsManager: TabsManager
    @Inject
    lateinit var searchProviders: SearchProviders

    @TargetApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val safeIntent = SafeIntent(intent)
        try {
            val action = safeIntent.action
            var text: String? = null
            when (action) {
                ACTION_SEND -> if (safeIntent.hasExtra(EXTRA_TEXT)) {
                    text = intent.extras?.getCharSequence(EXTRA_TEXT)?.toString()
                }
                ACTION_PROCESS_TEXT -> text = safeIntent.getStringExtra(EXTRA_PROCESS_TEXT)
            }
            if (text?.isNotEmpty() == true) {
                findAndOpenLink(text)
            }
        } catch (exception: Exception) {
            Timber.e(exception, "Could not process shared link")
            invalidLink()
        }
        // Do not finish here. In the URL case, the open must complete first;
        // search text may additionally require asynchronous provider lookup.
    }

    override fun inject(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    @SuppressLint("CheckResult")
    private fun findAndOpenLink(receivedText: String) {
        val urls = Utils.findURLs(receivedText)
        if (urls.isNotEmpty()) {
            // use only the first link
            val url = urls[0]
            openLink(url)
        } else {
            searchProviders.selectedProvider
                    .firstOrError()
                    .map { it.getSearchUrl(receivedText) }
                    .subscribeBy(
                            onSuccess = ::openLink,
                            onError = {
                                Timber.e(it, "Could not resolve shared search text")
                                invalidLink()
                            }
                    )
        }
    }

    private fun openLink(url: String?) {
        when (url) {
            null -> invalidLink()
            else -> {
                // Execute while this share receiver is still foreground.
                // The standard openUrl() queues a background task, which can
                // be rejected after this activity immediately finishes on
                // Android 14 (notably when Chrome initiated the share).
                tabsManager.openSharedUrl(this, Website(url)).subscribeBy(
                        onComplete = { finish() },
                        onError = {
                            Timber.e(it, "Failed to open shared link")
                            invalidLink()
                        }
                )
            }
        }
    }

    private fun invalidLink() {
        Toast.makeText(this, getString(R.string.invalid_link), Toast.LENGTH_SHORT).show()
        finish()
    }
}
