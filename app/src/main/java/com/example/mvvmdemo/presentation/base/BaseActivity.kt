package com.example.mvvmdemo.presentation.base

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.mvvmdemo.BuildConfig
import com.example.mvvmdemo.R
import com.example.mvvmdemo.utility.showDialog
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import retrofit2.HttpException
import javax.net.ssl.HttpsURLConnection
import kotlin.coroutines.CoroutineContext

abstract class BaseActivity : AppCompatActivity(), CoroutineScope {

    companion object {
        var dialogShowing = false
    }

    lateinit var job: Job
    private var progress: CustomProgressDialog? = null
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job


    abstract fun getBaseViewModel(): BaseViewModel?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()

        progress = CustomProgressDialog(this)

        attachBaseObserver()
    }

    private fun attachBaseObserver() {
        getBaseViewModel()?.exceptionLiveData?.observe(this, Observer {
            hideProgress()
            it?.apply {
                when (this) {
                    is HttpException -> {
                        when (this.code()) {
                            HttpsURLConnection.HTTP_UNAUTHORIZED -> showErrorDialog(getString(R.string.exception_error_unauthorized))
                            HttpsURLConnection.HTTP_FORBIDDEN -> showErrorDialog(getString(R.string.exception_error_forbidden))
                            HttpsURLConnection.HTTP_INTERNAL_ERROR -> showErrorDialog(getString(R.string.exception_error_server))
                            HttpsURLConnection.HTTP_BAD_REQUEST -> showErrorDialog(getString(R.string.exception_error_bad_request))
                            else -> this.localizedMessage
                        }
                    }
                    is JsonSyntaxException -> {
                        showErrorDialog(getString(R.string.exception_error_unparceble))
                    }
                    else -> {
                        if (BuildConfig.DEBUG) {
                            showErrorDialog(this.message!!)
                        } else {
                            showErrorDialog(getString(R.string.something_went_wrong))
                        }
                    }
                }
            }
        })
    }

    fun showErrorDialog(message: String) {
        showDialog(
            message,
            getString(R.string.ok), { dialog, _ ->
                dialog.dismiss()
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    protected fun addFragment(
        @IdRes containerViewId: Int, fragment: Fragment,
        fragmentTag: String
    ) {
        supportFragmentManager
            .beginTransaction()
            .add(containerViewId, fragment, fragmentTag)
            .disallowAddToBackStack()
            .commit()
    }

    protected fun replaceFragment(
        @IdRes containerViewId: Int, fragment: Fragment,
        fragmentTag: String,
        addToBackStack: Boolean? = false
    ) {
        supportFragmentManager
            .beginTransaction()
            .replace(containerViewId, fragment, fragmentTag)
            .addToBackStack(if (addToBackStack!!) fragment::class.java.simpleName else null)
            .commit()
    }

    protected fun replaceFragmentWithPop(
        @IdRes containerViewId: Int, fragment: Fragment,
        fragmentTag: String
    ) {
        supportFragmentManager
            .beginTransaction()
            .replace(containerViewId, fragment, fragmentTag)
            .disallowAddToBackStack()
            .commit()
    }


    fun showProgress() {
        if (!this.isFinishing) {
            progress?.show()
        }
    }

    fun hideProgress() {
        if (!this.isFinishing && progress?.isShowing == true) {
            progress?.dismiss()
        }
    }

}