package com.example.kotlinproject.view.news

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinproject.R
import com.example.kotlinproject.databinding.ActivityNewsBinding
import com.example.kotlinproject.global.common.GlobalUtility
import com.example.kotlinproject.global.constant.AppConstant
import com.example.kotlinproject.model.bean.newsChannel.NewsChanelRespo
import com.example.kotlinproject.view.adapter.NewsAdapter
import com.example.kotlinproject.view.base.BaseFragment
import com.example.kotlinproject.view.base.ScrollListener
import com.example.kotlinproject.view.profile.ProfileFragment
import com.example.kotlinproject.viewModel.list.NewsViewModel
import com.prodege.shopathome.model.networkCall.ApiResponse
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewsFragment : BaseFragment() {
    private var mLanguageCode: String = ""
    private var newsList: ArrayList<NewsChanelRespo.Source>? = null
    private lateinit var mBinding: ActivityNewsBinding
    private lateinit var newsAdapter: NewsAdapter
    private var progressDialog: ProgressDialog? = null
    private val mViewModel: NewsViewModel by viewModel()
    private var mPageCount: Int = 1

    companion object {
        val TAG = NewsFragment::class.java.simpleName
        fun getInstance(bundle: Bundle): NewsFragment {
            val fragment = NewsFragment()
            fragment.setArguments(bundle)
            return NewsFragment()
        }
    }

    override fun getLayout(): Int {
        return R.layout.activity_news
    }

    override fun onViewsInitialized(binding: ViewDataBinding?, view: View) {
        mBinding = binding as ActivityNewsBinding
        init()
        clickListener();
        setAdapter();
    }

    private fun init() {
        mBinding?.toolbar?.imgProfile?.visibility = View.VISIBLE
        mBinding?.toolbar?.txtToolbarTitle?.text = resources.getString(R.string.news_channel)
        mLanguageCode = preferenceMgr.getLanguageInfo().languageCode
        callApi()
    }

    private fun clickListener() {
        mBinding?.toolbar?.imgProfile?.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v?.id) {
            R.id.img_profile -> navigateScreen(ProfileFragment.TAG)
        }
    }

    private fun setAdapter() {
        newsAdapter = NewsAdapter(newsList)
        mBinding?.rvNewsChannel.layoutManager = LinearLayoutManager(activity)
        mBinding?.rvNewsChannel.addOnScrollListener(object :
            ScrollListener(mBinding.rvNewsChannel.getLayoutManager() as LinearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                mPageCount++
                callApi()
            }
        })
        mBinding?.rvNewsChannel.adapter = newsAdapter
    }

    private fun callApi() {
        progressDialog = ProgressDialog.show(activity, getString(R.string.please_wait), getString(R.string.loading))
        mViewModel?.getNewsChannelLiveData()?.observe(this, channelObserver)

        mViewModel?.newsChannelApi(
            "https://newsapi.org/v2/sources?language=" + mLanguageCode + "&page=" + mPageCount + "&pageSize=" + AppConstant.PAGINATION_SIZE + "&apiKey=" + getString(
                R.string.news_api_key
            )
        )
    }

    private val channelObserver: Observer<ApiResponse<NewsChanelRespo>> by lazy {
        Observer { response: ApiResponse<NewsChanelRespo> -> handleLoginResponse(response) }
    }

    private fun handleLoginResponse(response: ApiResponse<NewsChanelRespo>) {
        when (response?.status) {
            ApiResponse.Status.LOADING -> {
            }
            ApiResponse.Status.SUCCESS -> {
                if (progressDialog!!.isShowing) progressDialog?.dismiss()
                if (newsList == null || newsList?.size == 0) newsList = response.data!!.sources
                else newsList?.addAll(response.data!!.sources)
                newsAdapter.notifyAdapter(newsList!!)
                if (newsList == null || newsList?.size == 0)
                    mBinding.txtNoDataFound.visibility = View.VISIBLE
                else mBinding.txtNoDataFound.visibility = View.GONE
            }

            ApiResponse.Status.ERROR -> {
                if (progressDialog!!.isShowing) progressDialog!!.dismiss()
                GlobalUtility.showToast(getString(R.string.something_went_wrong))
            }
        }
    }

    /**
     * navigate on fragment
     * @param tag represent navigation activity
     */
    private fun navigateScreen(tag: String) {
        var frm: Fragment? = null
        if (tag == ProfileFragment.TAG)
            frm = ProfileFragment.getInstance(Bundle())
//          navigateFragment(R.id.container, frm!!, true)
        navigateAddFragment(R.id.container, frm!!, true);
    }

}


