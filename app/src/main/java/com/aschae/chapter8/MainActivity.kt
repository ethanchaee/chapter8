package com.aschae.chapter8

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.aschae.chapter8.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var imageAdapter: ImageAdapter
    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uriList ->
        updatePictures(uriList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.let {
            title = "사진 가져오기"
            setSupportActionBar(binding.toolbar)
        }

        binding.imageRecyclerView.run {
            imageAdapter = ImageAdapter(object : ItemClickListener {
                override fun onClickLoadMore() {
                    loadImageFromGallery()
                }
            })
            adapter = imageAdapter
            layoutManager = GridLayoutManager(context, 2)
        }

        binding.goFrameActivityButton.setOnClickListener {
            Intent(this, FrameActivity::class.java)
                .putExtra("images", imageAdapter.currentList.filterIsInstance<ImageItems.Image>().map { it.uri.toString() }.toTypedArray())
                .let { intent ->
                    startActivity(intent)
                }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add -> {
                loadImageFromGallery()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun loadImageFromGallery() {
        val storagePermission = Manifest.permission.READ_EXTERNAL_STORAGE
        if ((ContextCompat.checkSelfPermission(this, storagePermission) == PackageManager.PERMISSION_GRANTED)) {
            galleryLauncher.launch("image/*")
        } else if (shouldShowRequestPermissionRationale(storagePermission)) {
            showAlertDialog()
        } else {
            requestPermissions(arrayOf(storagePermission), REQUEST_READ_EXTRA_STORAGE_PERMISSION)
        }
    }

    private fun updatePictures(uriList: List<Uri>) {
        val firstIndex = imageAdapter.currentList.size
        val items = uriList.mapIndexed { index, item -> ImageItems.Image(item, "${firstIndex + index}-$item") }
        imageAdapter.submitList(imageAdapter.currentList.toMutableList().apply { addAll(items) })
        if (uriList.isNotEmpty()) binding.toolbar.title = "사진 (${imageAdapter.itemCount.dec()})"
    }

    private fun showAlertDialog() {
        AlertDialog.Builder(this).apply {
            setMessage("사진을 가져오려면 외부저장소의 읽기 권한이 있어야 합니다. 권한을 설정 하시겠습니까?")
            setPositiveButton("권한 설정") { _, _ ->
                Intent(ACTION_APPLICATION_DETAILS_SETTINGS).setData(
                    Uri.parse("package:${applicationContext.packageName}")
                ).let { startActivity(it) }
            }
            setNegativeButton("취소", null)
        }.show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_READ_EXTRA_STORAGE_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                    galleryLauncher.launch("images/*")
                } else {
                    Toast.makeText(this, "권한을 허락하지 않으면, 기능을 사용할 수 없습니다.", Toast.LENGTH_SHORT).show()
                }
                return
            }
            else -> {
                // Nothing to do
            }
        }
    }

    companion object {
        const val REQUEST_READ_EXTRA_STORAGE_PERMISSION = 221101
    }
}