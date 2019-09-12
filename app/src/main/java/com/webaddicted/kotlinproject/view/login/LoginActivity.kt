package com.webaddicted.kotlinproject.view.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.ActivityCommonBinding
import com.webaddicted.kotlinproject.view.base.BaseActivity

/**
 * Created by Deepak Sharma on 10/07/19.
 */
class LoginActivity : BaseActivity() {

    private lateinit var mBinding: ActivityCommonBinding
    companion object{
        val TAG: String = LoginActivity::class.java.simpleName
        fun newIntent(activity: Activity){
            activity.startActivity(Intent(activity, LoginActivity::class.java))
        }
    }
    override fun getLayout(): Int {
        return R.layout.activity_common
    }

    override fun initUI(binding: ViewDataBinding) {
        mBinding = binding as ActivityCommonBinding
        navigateScreen(LoginFragment.TAG)
    }

    /**
     * navigate on fragment
     *
     *
     * @param tag represent navigation activity
     */
    private fun navigateScreen(tag: String) {
        val frm: Fragment
        if (tag == LoginFragment.TAG) {
            frm = LoginFragment.getInstance(Bundle())
            navigateFragment(R.id.container, frm, false)
        }
    }
}