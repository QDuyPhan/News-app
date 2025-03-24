package com.example.newsapp.ui.widget

import android.content.Context
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.newsapp.R

class CustomToast(context: Context) : Toast(context) {

    companion object {
        const val SUCCESS = 1
        const val WARNING = 2
        const val ERROR = 3
        const val CONFUSING = 4

        const val SHORT_DURATION = 4000L
        const val LONG_DURATION = 7000L

//        fun makeText(
//            context: Context,
//            message: String,
//            duration: Int,
//            type: Int,
//            androidIcon: Boolean
//        ): Toast {
//            val toast = Toast(context)
//            toast.duration = duration
//            val layout =
//                LayoutInflater.from(context).inflate(R.layout.customtoast_layout, null, false)
//            val messageText = layout.findViewById<TextView>(R.id.toast_text)
//            val toastTypeLayout = layout.findViewById<LinearLayout>(R.id.toast_type)
//            val icon = layout.findViewById<ImageView>(R.id.toast_icon)
//            val androidIconView = layout.findViewById<ImageView>(R.id.imageView4)
//
//            messageText.text = message
//            androidIconView.visibility = if (androidIcon) View.VISIBLE else View.GONE
//
//            when (type) {
//                SUCCESS -> {
//                    toastTypeLayout.setBackgroundResource(R.drawable.success_shape)
//                    icon.setImageResource(R.drawable.check_icon)
//                }
//
//                WARNING -> {
//                    toastTypeLayout.setBackgroundResource(R.drawable.warning_shape)
//                    icon.setImageResource(R.drawable.warning_icon)
//                }
//
//                ERROR -> {
//                    toastTypeLayout.setBackgroundResource(R.drawable.error_shape)
//                    icon.setImageResource(R.drawable.error_icon)
//                }
//
//                CONFUSING -> {
//                    toastTypeLayout.setBackgroundResource(R.drawable.confusing_shape)
//                    icon.setImageResource(R.drawable.confusing_icon)
//                }
//            }
//
//            toast.view = layout
//            return toast
//        }

        fun makeText(
            context: Context,
            message: String,
            duration: Long,
            type: Int,
            imageResource: Int
        ): Toast {
            val toast = Toast(context)
            val layout =
                LayoutInflater.from(context).inflate(R.layout.customtoast_layout, null, false)
            val messageText = layout.findViewById<TextView>(R.id.toast_text)
            val toastTypeLayout = layout.findViewById<LinearLayout>(R.id.toast_type)
            val icon = layout.findViewById<ImageView>(R.id.toast_icon)

            messageText.text = message

            when (type) {
                SUCCESS -> {
                    toastTypeLayout.setBackgroundResource(R.drawable.success_shape)
                    icon.setImageResource(imageResource)
                }

                WARNING -> {
                    toastTypeLayout.setBackgroundResource(R.drawable.warning_shape)
                    icon.setImageResource(imageResource)
                }

                ERROR -> {
                    toastTypeLayout.setBackgroundResource(R.drawable.error_shape)
                    icon.setImageResource(imageResource)
                }

                CONFUSING -> {
                    toastTypeLayout.setBackgroundResource(R.drawable.confusing_shape)
                    icon.setImageResource(imageResource)
                }
            }

            toast.view = layout
            return toast
        }
    }
}