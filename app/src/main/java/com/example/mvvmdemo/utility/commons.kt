package com.example.mvvmdemo.utility

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageView
import com.example.mvvmdemo.presentation.base.BaseActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.mvvmdemo.BuildConfig
import com.example.mvvmdemo.R
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.priyank.snacyalert.SnacyAlert
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import java.math.RoundingMode
import java.text.DecimalFormat


fun View.gone() {
    this.visibility = View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

/**
 * Display the simple Toast message with the [Toast.LENGTH_SHORT] duration.
 *
 * @param message the message text.
 */
fun Context.toast(message: CharSequence): Toast = Toast
    .makeText(this, message, Toast.LENGTH_SHORT)
    .apply {
        show()
    }

fun View.snackbar(message: CharSequence) = Snackbar
    .make(this, message, Snackbar.LENGTH_SHORT)
    .apply { show() }

/**
 * For show dialog
 *
 * @param title - title which shown in dialog (application name)
 * @param msg - message which shown in dialog
 * @param positiveText - positive button text
 * @param listener - positive button listener
 * @param negativeText - negative button text
 * @param negativeListener - negative button listener
 * @param icon - drawable icon which shown is dialog
 */
fun Context.showDialog(
    msg: String,
    positiveText: String? = "OK",
    listener: DialogInterface.OnClickListener? = null,
    negativeText: String? = "Cancel",
    negativeListener: DialogInterface.OnClickListener? = null,
    title: String? = "Casali",
    icon: Int? = null
) {
    if (BaseActivity.dialogShowing) {
        return
    }
    val builder = AlertDialog.Builder(this)
    builder.setTitle(title)
    builder.setMessage(msg)
    builder.setCancelable(false)
    builder.setPositiveButton(positiveText) { dialog, which ->
        BaseActivity.dialogShowing = false
        listener?.onClick(dialog, which)
    }
    if (negativeListener != null) {
        builder.setNegativeButton(negativeText) { dialog, which ->
            BaseActivity.dialogShowing = false
            negativeListener.onClick(dialog, which)
        }
    }
    if (icon != null) {
        builder.setIcon(icon)
    }
    builder.create().show()
    BaseActivity.dialogShowing = true
}

/**
 * For validate email-id
 *
 * @return email-id is valid or not
 */
fun String.isValidEmail(): Boolean {
    return !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

/**
 * For validate phone
 *
 * @return phone is valid or not
 */
fun String.isValidPhone(): Boolean {
    return !TextUtils.isEmpty(this) && Patterns.PHONE.matcher(this).matches()
}

/**
 * isNetworkAvailable - Check if there is a NetworkConnection
 * @return boolean
 */
fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnected
}

/**
 * compare string
 * @return boolean
 */
infix fun String.shouldBeSame(other: String) = this == other

/**
 * For launch other activity
 */
inline fun <reified T : Any> Activity.launchActivity(
    requestCode: Int = -1,
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {}
) {

    val intent = newIntent<T>(this)
    intent.init()
    startActivityForResult(intent, requestCode, options)
}

inline fun <reified T : Any> Context.launchActivity(
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {}
) {

    val intent = newIntent<T>(this)
    intent.init()
    startActivity(intent, options)
}

inline fun <reified T : Any> newIntent(context: Context): Intent =
    Intent(context, T::class.java)


/**
 * load image
 */
fun Context.loadImage(imagePath: String, imageView: AppCompatImageView) {
    Glide.with(this)
        .load(imagePath)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(imageView)
}

fun Context.loadCircleImage(imagePath: String, imageView: AppCompatImageView) {
    Glide.with(this)
        .load(imagePath)
        .circleCrop()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(imageView)
}

fun Context.loadRoundedCornerImage(imagePath: String, imageView: AppCompatImageView) {
    val requestOptions = RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .priority(Priority.HIGH)

    Glide.with(this).load(imagePath)
        .transform(CenterCrop(), RoundedCorners(10))
        .apply(requestOptions)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(imageView)
}

/**
 * path from resources
 */
fun pathFromResources(resourceId: Int): String {
    return "android.resource://${BuildConfig.APPLICATION_ID}/$resourceId"
}

/**
 * prevent double click
 */
fun View.setOnSafeClickListener(
    onSafeClick: (View) -> Unit
) {
    setOnClickListener(SafeClickListener { v ->
        onSafeClick(v)
    })
}

/**
 * error toast
 */
@RequiresApi(Build.VERSION_CODES.O)
fun Activity.showErrorToast(message: CharSequence) {
    SnacyAlert.create(this)
        .setText(message)
        .setTextTypeface(resources.getFont(R.font.roboto_medium))
        .setBackgroundColorInt(Color.RED)
        //.setIcon(R.drawable.ic_close)
        .showIcon(true)
        .setDuration(2000)
        .show()
}

/**
 * for check string
 */
fun isEmptyString(text: String?): String {
    return if (text == null || text.trim { it <= ' ' } == "null" || text.trim { it <= ' ' }
            .isEmpty()) {
        "-"
    } else {
        text
    }
}

/**
 * value set in decimal format
 * like 1.1111 to 1.11
 */
fun setValueInDecimalFormat(value: Double): String {
    val df = DecimalFormat("#.##")
    df.roundingMode = RoundingMode.CEILING
    return df.format(value)
}

/**
 * convert object to string
 */
fun convertObjectToString(value: Any): String {
    return Gson().toJson(value)
}

/**
 * get object from string
 */
fun <T> convertStringToObject(value: String?, defValue: Class<T>): T {
    return Gson().fromJson(SharedPrefHelper[value!!, ""], defValue)
}

/**
 * convert to Requestbody
 */
fun convertToRequest(cID: String): RequestBody {
    return RequestBody.create("text/plain".toMediaTypeOrNull(), cID)
}




