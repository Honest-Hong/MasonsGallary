package com.mason.kakao.masonsgallary.ui.images

import android.Manifest
import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.Observable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.mason.kakao.masonsgallary.MasonApplication
import com.mason.kakao.masonsgallary.R
import com.mason.kakao.masonsgallary.databinding.FragmentImageListBinding
import com.mason.kakao.masonsgallary.model.data.ImageData
import com.mason.kakao.masonsgallary.ui.detail.OnImageDeleteListener
import com.mason.kakao.masonsgallary.ui.images.adapter.ImagesAdapter
import com.mason.kakao.masonsgallary.ui.images.adapter.OnShowDetailListener
import com.mason.kakao.masonsgallary.ui.images.viewmodel.ImageListViewModel
import java.util.ArrayList

/**
 * Created by kakao on 2017. 11. 3..
 */

class ImageListFragment : Fragment(), OnImageDeleteListener {
    lateinit var binding : FragmentImageListBinding
    lateinit var viewModel : ImageListViewModel
    lateinit var onShowDetailListener : OnShowDetailListener

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        onShowDetailListener = (context as OnShowDetailListener)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_image_list, container, false)
        setupViewModel()
        setupView()
        checkPermissions()
        return binding.root
    }

    override fun onDelete(imageData: ImageData) {
        for (data in viewModel.list) {
            if(data.path.equals(imageData.path)) {
                viewModel.list.remove(data)
                break
            }
        }
    }

    fun setupViewModel() {
        viewModel = ImageListViewModel(MasonApplication.get(context).imagesRepository)
        binding.viewModel = viewModel
        viewModel.detailViewData.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(p0: Observable?, p1: Int) {
                onShowDetailListener.onShowDetail(viewModel.detailViewData.get())
            }
        })
    }

    fun setupView() {
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = ImagesAdapter(context, viewModel)
    }

    companion object {
        @JvmStatic fun newInstance() : ImageListFragment {
            return ImageListFragment()
        }
    }

    private fun checkPermissions() {
        TedPermission.with(context)
                .setPermissionListener(object : PermissionListener {
                    override fun onPermissionGranted() {
                        viewModel.loadFirst();
                    }

                    override fun onPermissionDenied(deniedPermissions: ArrayList<String>) {}
                })
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .check()
    }
}