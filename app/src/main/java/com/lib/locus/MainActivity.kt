package com.lib.locus

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.lib.locus.adapter.DataAdapter
import com.lib.locus.listener.OnItemClickListener
import com.lib.locus.model.LocusDataModel
import com.lib.locus.model.ViewType
import com.lib.locus.viewmodel.MenuListViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject


class MainActivity : AppCompatActivity(), OnItemClickListener {

    companion object {
        const val USER_SELECTION_TAG = "USER_SELECTION_TAG"
        const val CAMERA_REQUEST_CODE = 88
    }

    private val adapter = DataAdapter(this);

    private var position: Int = 0
    val viewModel: MenuListViewModel by inject()


    private val menuListObserver: Observer<MutableList<LocusDataModel>> = Observer {
        adapter.locusDataModel = it
        recyclerView.adapter = adapter
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView.layoutManager = LinearLayoutManager(this)
        viewModel.dataModelList.observe(this, menuListObserver)
        viewModel.getProductDataList("locus_data.json")
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_REQUEST_CODE
            )
        } else {
            startCamera()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_REQUEST_CODE && (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            )
                    == PackageManager.PERMISSION_GRANTED)
        ) {
            startCamera()
        }
    }

    fun startCamera() {
        val intent = Intent("android.media.action.IMAGE_CAPTURE")
        startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE) {
            data?.extras?.get("data")?.let {
                adapter.setImageBitmap(position, it as Bitmap)
            }
        }
    }

    override fun onItemClicked(int: Int, imageBitmap: Bitmap?) {
        if (imageBitmap == null) {
            position = int
            checkCameraPermission()
        } else {
            ImageBitmapLocalCache.getInstance().imageBitmap = imageBitmap
            startActivity(Intent(this, ImageViewActivity::class.java))
        }
    }

    override fun onItemDeleted(int: Int) {
        adapter.setImageBitmap(int, null)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_submit, menu)
        val menuSearch = menu!!.findItem(R.id.action_submit)
        menuSearch.isVisible = true
        menuSearch.setOnMenuItemClickListener {
            onSubmitClicked(adapter.getDataModel())
            Toast.makeText(this, getString(R.string.text_logging_successful), Toast.LENGTH_LONG)
                .show()
            return@setOnMenuItemClickListener true
        }

        return true
    }

    private fun onSubmitClicked(locusDataModel: MutableList<LocusDataModel>?) {
        locusDataModel?.forEach {
            when (it.viewType) {
                ViewType.IMAGE -> {
                    Log.d(
                        USER_SELECTION_TAG,
                        "PhotoId = " + it.dataResponse?.id + "; Image = " + it.imageBitmap
                    )
                }
                ViewType.OPTIONS -> {
                    val string = it.selectPosition?.let { it1 ->
                        it.dataResponse?.dataMap?.options?.get(
                            it1
                        )
                    }
                    Log.d(
                        USER_SELECTION_TAG,
                        "SingleChoiceId = " + it.dataResponse?.id + "; Selected option = " + string
                    )
                }
                ViewType.COMMENT -> {
                    if (it.isCommentChecked == true) {
                        Log.d(
                            USER_SELECTION_TAG,
                            "CommentId = " + it.dataResponse?.id + "; Comment = " + it.comment
                        )
                    } else {
                        Log.d(
                            USER_SELECTION_TAG,
                            "CommentId = " + it.dataResponse?.id + "; Is Comment Provided? = " + false
                        )
                    }
                }
            }
        }
    }
}
