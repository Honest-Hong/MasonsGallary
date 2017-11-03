package com.mason.kakao.masonsgallary.ui.detail

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.Observable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mason.kakao.masonsgallary.ExtraKeys
import com.mason.kakao.masonsgallary.R
import com.mason.kakao.masonsgallary.databinding.FragmentImageDetailBinding
import com.mason.kakao.masonsgallary.model.data.ImageData

/**
 * Created by kakao on 2017. 11. 3..
 */

class ImageDetailFragment : Fragment() {
    lateinit var binding : FragmentImageDetailBinding
    lateinit var imageData : ImageData
    lateinit var viewModel : ImageDetailViewModel
    lateinit var onImageDeleteListener : OnImageDeleteListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imageData = arguments.getParcelable(ExtraKeys.IMAGE_DATA)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        onImageDeleteListener = (context as OnImageDeleteListener)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_image_detail, container, false)
        setupViewModel()
        return binding.root
    }

    fun setupViewModel() {
        viewModel = ImageDetailViewModel(imageData)
        binding.viewModel = viewModel
        viewModel.isDeleted.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(p0: Observable?, p1: Int) {
                if(viewModel.isDeleted.get()) {
                    onImageDeleteListener.onDelete(imageData)
                }
            }
        })
    }

    companion object {
        @JvmStatic fun newInstance(imageData: ImageData) : ImageDetailFragment {
            val fragment = ImageDetailFragment()
            val args = Bundle()
            args.putParcelable(ExtraKeys.IMAGE_DATA, imageData)
            fragment.arguments = args
            return fragment
        }
    }
}