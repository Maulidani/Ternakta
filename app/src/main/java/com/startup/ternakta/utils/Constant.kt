package com.startup.ternakta.utils

import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.google.android.material.button.MaterialButton
import com.startup.ternakta.R

object Constant {
//    const val BASE_URL = "http://192.168.88.5:8000"
    const val BASE_URL = "https://ternakta.com"
    const val IMAGE_URL_STORE = "$BASE_URL/image/user_store/"
    const val IMAGE_URL_CUSTOMER = "$BASE_URL/image/user_customer/"
    const val IMAGE_URL_PRODUCT = "$BASE_URL/image/product/"
    const val IMAGE_URL_ARTICLE = "$BASE_URL/image/article/"
    const val IMAGE_URL_TRANSACTION = "$BASE_URL/image/transaction_proof/"
    const val PHONE_ADMIN = "+62082344035420"

    fun MaterialButton.setShowProgress(showProgress: Boolean?) {

        iconGravity = MaterialButton.ICON_GRAVITY_TEXT_START
        isCheckable = showProgress == false
        text = if (showProgress == true) "" else "Coba lagi"

        icon = if (showProgress == true) {
            CircularProgressDrawable(context!!).apply {
                setStyle(CircularProgressDrawable.DEFAULT)
                setColorSchemeColors(ContextCompat.getColor(context!!, R.color.white))
                start()
            }
        } else null

        if (icon != null) { // callback to redraw button icon
            icon.callback = object : Drawable.Callback {
                override fun unscheduleDrawable(who: Drawable, what: Runnable) {
                }

                override fun invalidateDrawable(who: Drawable) {
                    this@setShowProgress.invalidate()
                }

                override fun scheduleDrawable(who: Drawable, what: Runnable, `when`: Long) {
                }
            }
        }
    }

}