package com.michaelfotiadis.locationmanagerviewer.ui.activity2.home

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.michaelfotiadis.locationmanagerviewer.R
import com.michaelfotiadis.locationmanagerviewer.databinding.ActivityHomeBinding
import com.michaelfotiadis.locationmanagerviewer.ui.activity2.home.ui.DepthPageTransformer
import com.michaelfotiadis.locationmanagerviewer.ui.activity2.home.ui.RecyclerPagerAdapter

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setSupportActionBar(binding.homeToolbar)

        val pagerAdapter = RecyclerPagerAdapter(this)

        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = pagerAdapter
        viewPager.setPageTransformer(DepthPageTransformer())
        val tabs: TabLayout = binding.tabs

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.setText(
                when (position) {
                    0 -> R.string.tab_text_1
                    else -> R.string.tab_text_2
                }
            )
        }.attach()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

}