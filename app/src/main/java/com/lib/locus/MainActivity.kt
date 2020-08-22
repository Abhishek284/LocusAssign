package com.lib.locus

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

    private fun startCamera() {
        val intent = Intent("android.media.action.IMAGE_CAPTURE")
        startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE) {
            val imageBitmap: Bitmap = data?.extras?.get("data") as Bitmap
            adapter.setImageBitmap(position, imageBitmap)

        }
    }

    override fun onItemClicked(int: Int, imageBitmap: Bitmap?) {
        if (imageBitmap == null) {
            position = int
            startCamera()
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
            Toast.makeText(this, getString(R.string.text_logging_successful), Toast.LENGTH_LONG).show()
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
                        "PhotoId = " + it.dataResponse?.id + " value = " + it.imageBitmap
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
                        "SingleChoiceId = " + it.dataResponse?.id + " value = " + string
                    )
                }
                ViewType.COMMENT -> {
                    Log.d(
                        USER_SELECTION_TAG,
                        "CommentId = " + it.dataResponse?.id + " value = " + it.comment
                    )
                }
            }
        }
    }
}
