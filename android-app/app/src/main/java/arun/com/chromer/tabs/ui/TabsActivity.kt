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

package arun.com.chromer.tabs.ui

import android.os.Bundle
import android.view.MenuItem
import arun.com.chromer.R
import arun.com.chromer.di.activity.ActivityComponent
import arun.com.chromer.shared.FabHandler
import arun.com.chromer.shared.base.Snackable
import arun.com.chromer.shared.base.activity.BaseActivity
import arun.com.chromer.shared.base.activity.TargetedSystemBarInsets
import butterknife.OnClick
import com.google.android.material.snackbar.Snackbar
import arun.com.chromer.databinding.ActivityTabsBinding
import android.view.ViewGroup

class TabsActivity : BaseActivity(), Snackable {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityTabsBinding.bind(findViewById<ViewGroup>(android.R.id.content).getChildAt(0))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Keep the bottom-right action button above the Android 16 taskbar
        // while keeping the toolbar below the status bar.
        TargetedSystemBarInsets.protect(this, binding.coordinatorLayout)
        setSupportActionBar(binding.toolbar)

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.article_ic_close)
        }

        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, TabsFragment())
                    .commit()
        }
    }

    @OnClick(R.id.fab)
    fun onFabClick() {
        supportFragmentManager.fragments
                .asSequence()
                .filter { !it.isHidden && it is FabHandler }
                .map { it as FabHandler }
                .first()
                .onFabClick()
    }

    override fun getLayoutRes() = R.layout.activity_tabs

    override fun snack(textToSnack: String) {
        Snackbar.make(binding.coordinatorLayout, textToSnack, Snackbar.LENGTH_SHORT).show()
    }

    override fun snackLong(textToSnack: String) {
        Snackbar.make(binding.coordinatorLayout, textToSnack, Snackbar.LENGTH_LONG).show()
    }

    override fun onOptionsItemSelected(item: MenuItem) = when {
        item.itemId == android.R.id.home -> {
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun inject(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }
}
