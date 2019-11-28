package com.webaddicted.kotlinproject.view.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmTaskListBinding
import com.webaddicted.kotlinproject.global.common.ShowSearchView
import com.webaddicted.kotlinproject.global.common.gone
import com.webaddicted.kotlinproject.global.common.visible
import com.webaddicted.kotlinproject.view.activity.MapActivity
import com.webaddicted.kotlinproject.view.activity.NavigationDrawerActivity
import com.webaddicted.kotlinproject.view.activity.SpeechTextActivity
import com.webaddicted.kotlinproject.view.activity.WebViewActivity
import com.webaddicted.kotlinproject.view.adapter.TaskAdapter
import com.webaddicted.kotlinproject.view.base.BaseFragment
import com.webaddicted.kotlinproject.view.ecommerce.EcommLoginFrm
import java.util.*
import kotlin.collections.ArrayList

class TaskFrm : BaseFragment() {
    private lateinit var mBinding: FrmTaskListBinding
    private lateinit var mHomeAdapter: TaskAdapter
    private var mTaskList: ArrayList<String>? = null
    internal var worktask = arrayOf(
        "Widgets",
        "News Api",
        "Google Map / Location",
        "Circle Game",
        "Calendar View",
        "SMS Retriever",
        "Webview",
        "Dialog",
        "Select Multiple Image",
        "Dynamic Layout",
        "Shared Preference",
        "Device Info",
        "Speech to text",
        "Animation",
        "Recycler View",
        "Expendable/Spinner List View",
        "Share",
        "Receiver",
        "Services",
        "Viewpager Tab",
        "FingerPrint",
        "Barcode",
        "Timer",
//        "BlinkScan",
        "Ecommerce",
        "Navigation Drawer",

        "Digital Signature",
        "PDF",
        "Collapse",
        "UI Design",
        "Fab Button",
        "Bottom Navigation",
        "Bottom Sheet"
    )
    lateinit var showSearchView: ShowSearchView

    companion object {
        val TAG = TaskFrm::class.java.simpleName
        fun getInstance(bundle: Bundle): TaskFrm {
            val fragment = TaskFrm()
            fragment.arguments = bundle
            return TaskFrm()
        }
    }

    override fun getLayout(): Int {
        return R.layout.frm_task_list
    }

    override fun onViewsInitialized(binding: ViewDataBinding?, view: View) {
        mBinding = binding as FrmTaskListBinding
        init()
    }

    private fun init() {
        mBinding.toolbar.imgBack.gone()
        mBinding.toolbar.imgProfile.visible()
        mBinding.toolbar.imgProfile.setImageDrawable(resources.getDrawable(R.drawable.ic_search))
        mBinding.toolbar.txtToolbarTitle.text = resources.getString(R.string.task_title)
        mTaskList = ArrayList(Arrays.asList(*worktask))
        showSearchView = ShowSearchView()
        setAdapter()
        clickListener()

    }

    private fun clickListener() {
        mBinding.toolbar.imgProfile.setOnClickListener(this)
        mBinding.toolbar.imgSearchBack.setOnClickListener(this)
        mBinding.linearMobileNo.setOnClickListener(this)
        mBinding.toolbar.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                val text = mBinding.toolbar.editTextSearch.getText().toString()
                    .toLowerCase(Locale.getDefault())
                mHomeAdapter.filter(text)
            }

            override fun afterTextChanged(editable: Editable) {
            }
        })

    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.img_search_back, R.id.img_profile ->
                showSearchView.handleToolBar(
                    activity,
                    mBinding.toolbar.cardSearch,
                    mBinding.toolbar.editTextSearch
                )
            R.id.linear_mobile_no-> {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:" + getString(R.string.deep_mobile_no))
                startActivity(intent)
            }
        }
    }

    private fun setAdapter() {
        mHomeAdapter = TaskAdapter(this@TaskFrm, mTaskList)
        mBinding.recyclerView.setLayoutManager(
            LinearLayoutManager(
                activity,
                LinearLayoutManager.VERTICAL,
                false
            )
        )
        mBinding.recyclerView.setAdapter(mHomeAdapter)
    }

    fun onClicks(click: String) {
        when (click) {
            "Widgets" -> navigateScreen(WidgetFrm.TAG)
            "News Api" -> navigateScreen(NewsFrm.TAG)
            "Google Map / Location" -> navigateScreen(MapActivity.TAG)
            "Circle Game" -> navigateScreen(CircleFrm.TAG)
            "Calendar View" -> navigateScreen(CalendarFrm.TAG)
            "SMS Retriever" -> navigateScreen(SmsRetrieverFrm.TAG)
            "Webview" -> navigateScreen(WebViewActivity.TAG)
            "Dialog" -> navigateScreen(DialogFrm.TAG)
            "Select Multiple Image" -> navigateScreen(ProfileFrm.TAG)
            "Dynamic Layout" -> navigateScreen(DynamicLayoutFrm.TAG)
            "Shared Preference" -> navigateScreen(SharedPrefFrm.TAG)
            "Speech to text" -> navigateScreen(SpeechTextActivity.TAG)
            "Animation" -> navigateScreen(AnimationFrm.TAG)
            "Share" -> navigateScreen(ShareDataFrm.TAG)
            "Recycler View" -> navigateScreen(RecyclerViewFrm.TAG)
            "Expendable/Spinner List View" -> navigateScreen(ExpendableFrm.TAG)
            "Receiver" -> navigateScreen(ReceiverFrm.TAG)
            "Services" -> navigateScreen(ServiceFrm.TAG)
            "Ecommerce" -> navigateScreen(EcommLoginFrm.TAG)
            "Timer" -> navigateScreen(TimerFrm.TAG)
            "Navigation Drawer" ->navigateScreen(NavigationDrawerActivity.TAG)
//            "BlinkScan" -> navigateScreen(BlinkScanFrm.TAG)
            else -> navigateScreen(WidgetFrm.TAG)
        }
    }

    override fun onResume() {
        super.onResume()
        addBlankSpace(mBinding.bottomSpace)
    }

    /**
     * navigate on fragment
     *
     * @param tag represent navigation activity
     */
    fun navigateScreen(tag: String) {
        var frm: Fragment? = null
        when (tag) {
            WidgetFrm.TAG -> frm = WidgetFrm.getInstance(Bundle())
            NewsFrm.TAG -> frm = NewsFrm.getInstance(Bundle())
            MapActivity.TAG -> activity?.let { MapActivity.newIntent(it) }
            CircleFrm.TAG -> frm = CircleFrm.getInstance(Bundle())
            CalendarFrm.TAG -> frm = CalendarFrm.getInstance(Bundle())
            SmsRetrieverFrm.TAG -> frm = SmsRetrieverFrm.getInstance(Bundle())
            WebViewActivity.TAG -> activity?.let { WebViewActivity.newIntent(it) }
            DialogFrm.TAG -> frm = DialogFrm.getInstance(Bundle())
            ProfileFrm.TAG -> frm = ProfileFrm.getInstance(Bundle())
            DynamicLayoutFrm.TAG -> frm = DynamicLayoutFrm.getInstance(Bundle())
            SharedPrefFrm.TAG -> frm = SharedPrefFrm.getInstance(Bundle())
            AnimationFrm.TAG -> frm = AnimationFrm.getInstance(Bundle())
            ShareDataFrm.TAG -> frm = ShareDataFrm.getInstance(Bundle())
            SpeechTextActivity.TAG -> activity?.let { SpeechTextActivity.newIntent(it) }
            RecyclerViewFrm.TAG -> frm = RecyclerViewFrm.getInstance(Bundle())
            ExpendableFrm.TAG -> frm = ExpendableFrm.getInstance(Bundle())
            ReceiverFrm.TAG -> frm = ReceiverFrm.getInstance(Bundle())
            ServiceFrm.TAG -> frm = ServiceFrm.getInstance(Bundle())
            EcommLoginFrm.TAG -> frm = EcommLoginFrm.getInstance(Bundle())
            TimerFrm.TAG -> frm = TimerFrm.getInstance(Bundle())
            BlinkScanFrm.TAG -> frm = BlinkScanFrm.getInstance(Bundle())
            NavigationDrawerActivity.TAG -> activity?.let { NavigationDrawerActivity.newIntent(it) }
            else -> frm = WidgetFrm.getInstance(Bundle())
        }
        frm?.let { navigateAddFragment(R.id.container, it, true) }
    }
}