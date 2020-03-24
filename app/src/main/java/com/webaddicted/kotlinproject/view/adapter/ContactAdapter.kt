package com.webaddicted.kotlinproject.view.adapter

import android.graphics.Color
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import androidx.databinding.ViewDataBinding
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.RowContactBinding
import com.webaddicted.kotlinproject.global.common.gone
import com.webaddicted.kotlinproject.global.common.visible
import com.webaddicted.kotlinproject.model.bean.ContactBean
import com.webaddicted.kotlinproject.view.base.BaseAdapter
import java.util.*

/**
 * Created by Deepak Sharma on 01/07/19.
 */
class ContactAdapter(private var list: ArrayList<ContactBean>?) : BaseAdapter() {
    public var searchText: String? = null
    private var searchArray: ArrayList<ContactBean>

    init {
        this.searchArray = ArrayList()
        list?.let { this.searchArray.addAll(it) }
    }

    override fun getListSize(): Int {
        if (list == null) return 0
        return list?.size!!
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.row_contact
    }

    override fun onBindTo(rowBinding: ViewDataBinding, position: Int) {
        if (rowBinding is RowContactBinding) {
            val source = list?.get(position)
            if (source?.contactName != null && source.contactName.isNotEmpty()) {
                rowBinding.txtName.visible()
                rowBinding.txtName.text = source.contactName
            } else rowBinding.txtName.gone()

            if (source?.contactEmail != null && source.contactEmail.isNotEmpty()) {
                rowBinding.txtEmail.visible()
                source.contactEmail=source.contactEmail.replace("\n","<br/>")
                rowBinding.txtEmail.text = Html.fromHtml("<font color=\"#000000\">Email Id</font></br/> \n${source.contactEmail}".trimIndent())
            } else rowBinding.txtEmail.gone()

            if (source?.contactNumber != null && source.contactNumber.isNotEmpty()) {
                rowBinding.txtNo.visible()
                source.contactNumber = source.contactNumber.replace("\n","<br/>")
                rowBinding.txtNo.text = Html.fromHtml("<font color=\"#000000\">Contact Number</font><br/> \n${source.contactNumber}".trimIndent())
            } else rowBinding.txtNo.gone()

            if (source?.contactInfo != null && source.contactInfo.isNotEmpty()) {
                rowBinding.txtOtherInfo.visible()
                source.contactInfo =  source.contactInfo.replace("\n","<br/>")
                rowBinding.txtOtherInfo.text = Html.fromHtml("<font color=\"#000000\">Other Details</font><br/> \n${source.contactInfo}".trimIndent())
            } else rowBinding.txtOtherInfo.gone()

            if (source?.checked === "0") {
                Log.d(
                    "aaaaaaaaaaaaa",
                    "start page source.getChecked()==0   " + source
                        .checked + "  name===>" + source
                        .contactName
                )
                rowBinding.cb.isChecked = false
            } else {
                Log.d(
                    "aaaaaaaaaaaaa",
                    "start page source.getChecked()==1   " + source
                        ?.checked.toString() + "   name===>" + source
                        ?.contactName
                )
                rowBinding.cb.isChecked = true
            }
            if (source?.contactPhoto != null)
                rowBinding.contactImage.setImageBitmap(source.contactPhoto)
            else
                rowBinding.contactImage.setImageDrawable(mContext.resources.getDrawable(R.drawable.girl))
            onClickListener(rowBinding, rowBinding.cb, position)
        }
    }

    override fun getClickEvent(rowBinding: ViewDataBinding, view: View?, position: Int) {
        super.getClickEvent(rowBinding, view, position)
        rowBinding as RowContactBinding
        val source = list?.get(position)
        when (view?.id) {
            R.id.cb -> {
                if (rowBinding.cb.isChecked) {
                    source?.checked = "1"
                    Log.d(
                        "aaaaaaaaaaaaa",
                        "checked ===> 1   " + source?.checked
                            .toString() + " " + source?.contactName
                    )
                } else {
                    source?.checked = "0"
                    Log.d(
                        "aaaaaaaaaaaaa",
                        "checked ===> 0   " + source?.checked
                            .toString() + " " + source?.contactName
                    )
                }
            }
        }
    }

    fun filter(charText: String?) {
        var charText = charText
        charText = charText!!.toLowerCase(Locale.getDefault())
        searchText = charText
        list?.clear()
        if (charText == null && charText!!.length == 0) {
            list?.addAll(searchArray)
        } else {
            for (wp in searchArray) {
                when {
                    wp.contactName .toLowerCase(Locale.getDefault()).contains(charText) ||
                            wp.contactNumber.toLowerCase(Locale.getDefault())
                                .contains(charText) -> {
                        list?.add(wp)
                    }
                }
            }
        }
        notifyDataSetChanged()
    }

    fun notifyAdapter(prodList: ArrayList<ContactBean>) {
        list = prodList
        searchArray = ArrayList()
        list?.let { searchArray.addAll(it) }
        notifyDataSetChanged()
    }
}