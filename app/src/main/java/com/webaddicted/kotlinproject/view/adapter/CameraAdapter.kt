package com.webaddicted.kotlinproject.view.adapter

import androidx.databinding.ViewDataBinding
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.RowCameraItemBinding
import com.webaddicted.kotlinproject.model.camera.CameraBean
import com.webaddicted.kotlinproject.view.base.BaseAdapter

/**
 * Created by Deepak Sharma on 01/07/19.
 */
class CameraAdapter( private var list: java.util.ArrayList<CameraBean>?) : BaseAdapter() {
    override fun getListSize(): Int {
        if (list == null) return 0
        return list?.size!!

    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.row_camera_item
    }

    override fun onBindTo(mRowBinding: ViewDataBinding, position: Int) {
        if (mRowBinding is RowCameraItemBinding) {
            val source = list?.get(position)
            mRowBinding.txtCameraFeatureValue.text = source?.featureValue
            mRowBinding.txtCameraFeatureName.text = source?.featureLable
        }
    }

    fun notifyAdapter(prodList: ArrayList<CameraBean>) {
        list = prodList
        notifyDataSetChanged()
    }
}