package com.aschae.chapter8

import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.aschae.chapter8.databinding.ActivityFrameBinding
import com.google.android.material.tabs.TabLayoutMediator

class FrameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFrameBinding
    private val frameAdapter = FrameAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFrameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        binding.toolbar.let { toolbar ->
            toolbar.title = "나만의 앨범"
            setSupportActionBar(toolbar)
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val items = (intent.getStringArrayExtra("images") ?: emptyArray()).mapIndexed { index, uri -> ImageItems.Image(Uri.parse(uri), "$index-$uri") }
        binding.viewPager.adapter = frameAdapter
        frameAdapter.submitList(items)

        TabLayoutMediator(
            binding.tabIndicator,
            binding.viewPager
        )
        { tab, _ ->
            binding.viewPager.currentItem = tab.position
        }.attach()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}