package com.webaddicted.kotlinproject.global.common

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.*
import android.media.RingtoneManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.format.DateFormat
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.bumptech.glide.DrawableTypeRequest
import com.bumptech.glide.Glide
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.global.common.AppApplication.Companion.context
import com.webaddicted.kotlinproject.global.common.AppApplication.Companion.mCurrencyActivity
import com.webaddicted.kotlinproject.global.constant.AppConstant.Companion.NOTIFICATION_CHANNEL_ID
import com.webaddicted.kotlinproject.view.home.HomeActivity
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.File
import java.lang.reflect.Modifier
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Deepak Sharma on 01/07/19.
 */
class GlobalUtility {

    companion object {

        fun showToast(message: String) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }

        fun getDate(context: Context, mDobEtm: TextView) {
            val datePickerDialog = DatePickerDialog(context, R.style.TimePicker, { view, year, month, dayOfMonth ->
                mDobEtm.text =
                    "$dayOfMonth/$month/$year"
            }, Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH)
            datePickerDialog.show()
        }

        fun timePicker(timeListener: TimePickerDialog.OnTimeSetListener): TimePickerDialog {
            val calendar = Calendar.getInstance()
            return TimePickerDialog(
                mCurrencyActivity,
                R.style.TimePicker,
                timeListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                DateFormat.is24HourFormat(context)
            )
        }

        /**
         * convert date formate
         *
         * @param date         date any formate string
         * @param inputFormat  input date formate
         * @param outputFormat output date formate
         * @return output date formate
         */
        fun dateFormate(date: String, inputFormat: String, outputFormat: String): String {
            var initDate: Date? = null
            try {
                initDate = SimpleDateFormat(inputFormat).parse(date)
            } catch (e: java.text.ParseException) {
                e.printStackTrace()
            }

            val formatter = SimpleDateFormat(outputFormat)
            return formatter.format(initDate)
        }

        /**
         * convertTime formate
         *
         * @param timeHHMM
         * @return
         */
        fun timeFormat12(timeHHMM: String): String {
            try {
                val sdf = SimpleDateFormat("H:mm")
                val dateObj = sdf.parse(timeHHMM)

                return SimpleDateFormat("K:mm: a").format(dateObj)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return ""
        }

        /**
         * convertTime formate
         *
         * @param timeHHMM
         * @return
         */
        fun timeFormat24(timeHHMM: String): String {
            val sdf = SimpleDateFormat("hh:mm a")
            var testDate: Date? = null
            try {
                testDate = sdf.parse(timeHHMM)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

            val formatter = SimpleDateFormat("HH:MM")
            return formatter.format(testDate)
        }
//    {START HIDE SHOW KEYBOARD}

        /**
         * Method to hide keyboard
         *
         * @param activity Context of the calling class
         */
        fun hideKeyboard(activity: Activity) {
            try {
                val inputManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
            } catch (ignored: Exception) {
                Log.d("TAG", "hideKeyboard: " + ignored.message)
            }

        }

        /***
         * Show SoftInput Keyboard
         * @param activity reference of current activity
         */
        fun showKeyboard(activity: Activity?) {
            if (activity != null) {
                val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm?.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
            }
        }

//    {END HIDE SHOW KEYBOARD}


//      {START STRING TO JSON & JSON TO STRING}

        /**
         * @param json  json String converted by Gson to string
         * @param clazz referance of class type like MyBean.class
         * @param <T>
         * @return bean referance
        </T> */
        fun <T> stringToJson(json: String, clazz: Class<T>): T {
            return GsonBuilder()
                .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                .create().fromJson(json, clazz)
        }

        /**
         * @param clazz referance of any bean
         * @return
         */
        fun jsonToString(clazz: Class<*>): String {
            return Gson().toJson(clazz)
        }

        //{END STRING TO JSON & JSON TO STRING}

        /**
         * Gets network state.
         *
         * @return the network state
         */
        fun isNetworkAvailable(): Boolean {
            val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = connMgr?.activeNetworkInfo
            return activeNetwork != null && activeNetwork.isAvailable && activeNetwork.isConnected
        }

        /**
         * show internet connection toast
         */
        fun showNoNetworkToast() {
            val msg = context.getResources().getString(R.string.no_network_msg)
            showToast(msg)
        }


//    {START SHOW IMAGE USING GLIDE}

        /**
         * show image using glide
         *
         * @param imagePath       image url
         * @param targetIv        image view
         * @param imageLoaderType type of place holder show which is define in string
         */
        fun showImageUsingGLIDE(targetIv: ImageView,imagePath: String,  imageLoaderType: String) {
            val drawableTypeRequest = Glide.with(targetIv.context)
                .load(imagePath)
            getPlaceHolder(drawableTypeRequest, imageLoaderType).into(targetIv)
        }


        //block up when loder show on screen
        /**
         * handle ui
         *
         * @param activity
         * @param view
         * @param isBlockUi
         */
        fun handleUI(activity: Activity, view: View, isBlockUi: Boolean) {
            if (isBlockUi) {
                activity.window.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                )
                view.visibility = View.VISIBLE
            } else {
                activity.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                view.visibility = View.GONE
            }
        }

        /**
         * show image using glide
         *
         * @param imgUrl          local image file path
         * @param targetIv        image view
         * @param imageLoaderType type of place holder show which is define in string
         */
        fun showImageUsingGLIDE(imgUrl: File, targetIv: ImageView, imageLoaderType: String) {
            val drawableTypeRequest = Glide.with(targetIv.context)
                .load(Uri.fromFile(imgUrl))
            getPlaceHolder(drawableTypeRequest, imageLoaderType).into(targetIv)
        }

        /**
         * apply different type of place holder
         *
         * @param drawableTypeRequest
         * @param imageLoaderType     type of place holder show
         * @param <T>
         * @return
        </T> */
        private fun <T> getPlaceHolder(
            drawableTypeRequest: DrawableTypeRequest<T>,
            imageLoaderType: String
        ): DrawableTypeRequest<T> {
            val imageLoadersArray = context.getResources().getStringArray(R.array.image_loader)
            if (imageLoadersArray[0] == imageLoaderType) {
                drawableTypeRequest.error(R.drawable.logo)
                drawableTypeRequest.placeholder(R.drawable.logo)
            } else if (imageLoadersArray[1] == imageLoaderType) {
                drawableTypeRequest.error(R.drawable.ic_flag)
                drawableTypeRequest.placeholder(R.drawable.ic_flag)
            }  else if (imageLoadersArray[2] == imageLoaderType) {
                drawableTypeRequest.error(R.drawable.news_paper)
                drawableTypeRequest.placeholder(R.drawable.news_paper)
            } else {
                drawableTypeRequest.error(R.color.app_color)
                drawableTypeRequest.placeholder(R.color.app_color)
            }
            return drawableTypeRequest
        }

        //    {END SHOW IMAGE USING GLIDE}

        /**
         * provide binding of layout
         *
         * @param context reference of activity
         * @param layout  layout
         * @return viewBinding
         */
        fun getLayoutBinding(context: Context, layout: Int): ViewDataBinding {
            return DataBindingUtil.inflate(
                LayoutInflater.from(context),
                layout,
                null, false
            )
        }

        /**
         * @param sizeOfRandomString length of random string
         * @return generate a random string
         */
        fun getRandomString(sizeOfRandomString: Int): String {
            val ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm"
            val random = Random()
            val sb = StringBuilder(sizeOfRandomString)
            for (i in 0 until sizeOfRandomString)
                sb.append(ALLOWED_CHARACTERS[random.nextInt(ALLOWED_CHARACTERS.length)])
            return sb.toString()
        }


        /**
         * two digit random number
         *
         * @return random number
         */
        fun getTwoDigitRandomNo(): Int {
            return Random().nextInt(90) + 10
        }


        /**
         * show string in different color using spannable
         *
         * @param textView     view
         * @param txtSpannable string text
         * @param starText     start index of text
         * @param endText      end index of text
         */
        fun setSpannable(textView: TextView, txtSpannable: String, starText: Int, endText: Int) {
            val spannableString = SpannableString(txtSpannable)
            val foregroundSpan = ForegroundColorSpan(Color.GREEN)
            //            BackgroundColorSpan backgroundSpan = new BackgroundColorSpan(Color.GRAY);
            spannableString.setSpan(foregroundSpan, starText, endText, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            //            spannableString.setSpan(backgroundSpan, starText, endText, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.text = spannableString
        }

        /**
         * button click fade animation
         *
         * @param view view reference
         */
        fun btnClickAnimation(view: View) {
            val fadeAnimation = AnimationUtils.loadAnimation(view.context, R.anim.fade_in)
            view.startAnimation(fadeAnimation)
        }

        fun changeLanguage(context: Context, lancuageCode: String) {
//            val languageToLoad = "en" // your language
            val locale = Locale(lancuageCode)
            Locale.setDefault(locale)
            val config = Configuration()
            config.locale = locale
            context.getResources().updateConfiguration(
                config,
                context.getResources().getDisplayMetrics()
            )
        }

        /**
         * Adds a watermark on the given image.
         */
        fun addWatermark(res: Resources, source: Bitmap, mScreenwidth: Int): Bitmap {
            val w: Int
            val h: Int
            val c: Canvas
            val paint: Paint
            val bmp: Bitmap
            val watermark: Bitmap
            val matrix: Matrix
            val scale: Float
            val r: RectF
            w = source.width
            h = source.height
            // Create the new bitmap
            bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG or Paint.FILTER_BITMAP_FLAG)
            // Copy the original bitmap into the new one
            c = Canvas(bmp)
            c.drawBitmap(source, 0f, 0f, paint)
            // Load the watermark
            watermark = BitmapFactory.decodeResource(res, R.drawable.logo)
            // Scale the watermark to be approximately 40% of the source image height
            scale = (h.toFloat() * 0.15 / watermark.height.toFloat()).toFloat()
            val scaleX = w.toFloat() / watermark.width.toFloat()
            // Create the matrix
            matrix = Matrix()
            matrix.postScale(scaleX, scale)
            // Determine the post-scaled size of the watermark
            r = RectF(0f, 0f, watermark.width.toFloat(), watermark.height.toFloat())
            matrix.mapRect(r)
            // Move the watermark to the bottom right corner
            matrix.postTranslate(w - r.width(), h - r.height())
            // Draw the watermark
            c.drawBitmap(watermark, matrix, paint)
            // Free up the bitmap memory
            watermark.recycle()
            return bmp
        }

        fun showOfflineNotification(context: Context, title:String, description: String) {
            val intent = Intent(context, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            if (intent != null) {
                val pendingIntent = PendingIntent.getActivity(
                    context, getTwoDigitRandomNo(), intent,
                    PendingIntent.FLAG_ONE_SHOT
                )
                val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val notificationBuilder = NotificationCompat.Builder(context)
                notificationBuilder.setSmallIcon(R.mipmap.ic_launcher_round)
                notificationBuilder.setLargeIcon(
                    BitmapFactory.decodeResource(
                        context.resources,
                        R.mipmap.ic_launcher
                    )
                )
                notificationBuilder.setBadgeIconType(R.mipmap.ic_launcher_round)
                notificationBuilder.setContentTitle(title)
                if (description != null) {
                    notificationBuilder.setContentText(description)
                    notificationBuilder.setStyle(
                        NotificationCompat.BigTextStyle().bigText(description)
                    )
                }
                notificationBuilder.setAutoCancel(true)
                notificationBuilder.setSound(defaultSoundUri)
                notificationBuilder.setVibrate(longArrayOf(1000, 1000))
                notificationBuilder.setContentIntent(pendingIntent)
                val notificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val importance = NotificationManager.IMPORTANCE_HIGH
                    val notificationChannel = NotificationChannel(
                        NOTIFICATION_CHANNEL_ID,
                        "NOTIFICATION_CHANNEL_NAME",
                        importance
                    )
                    notificationChannel.enableLights(true)
                    notificationChannel.lightColor = Color.RED
                    notificationChannel.enableVibration(true)
                    notificationChannel.vibrationPattern = longArrayOf(1000, 1000)
                    assert(notificationManager != null)
                    notificationBuilder.setChannelId(NOTIFICATION_CHANNEL_ID)
                    notificationManager.createNotificationChannel(notificationChannel)
                }
                notificationManager.notify(
                    getTwoDigitRandomNo()/*Id of Notification*/,
                    notificationBuilder.build()
                )
            }
        }
//        fun showNotification(context: Context,text: String, bigText: String) {
//
//            // 1. Create a NotificationManager
//            val notificationManager =
//                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//            // 2. Create a PendingIntent for AllGeofencesActivity
//            val intent = Intent(context, HomeActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            val pendingNotificationIntent =
//                PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
//
//            // 3. Create and send a notification
//            val notification = NotificationCompat.Builder(context)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle("Title")
//                .setContentText(text)
//                .setContentIntent(pendingNotificationIntent)
//                .setStyle(NotificationCompat.BigTextStyle().bigText(bigText))
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setAutoCancel(true)
//                .build()
//            notificationManager.notify(0, notification)
//        }
    }


}