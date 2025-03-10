package com.example.newsapp.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.newsapp.R
import com.example.newsapp.utils.setOnSingClickListener

class ActionBarView(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {
    private var actionLeft: ImageView
    private var actionRight: ImageView
    private var tvTitle: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_action_bar, this, true)
        actionLeft = findViewById(R.id.actionLeft)
        actionRight = findViewById(R.id.actionRight)
        tvTitle = findViewById(R.id.tvTitle)
        attrs?.let { initAttr(it) }
    }

//    fun setImageIcon(icon: ImageView) {
//        actionRight.setImageDrawable(icon)
//    }

    fun setTitle(title: String) {
        tvTitle.text = title
    }

    fun setTitleColor(@ColorRes color: Int) {
        tvTitle.setTextColor(ContextCompat.getColor(context, color))
    }

    fun setOnClickLeft(callback: (() -> Unit)? = null) {
        actionLeft.setOnSingClickListener { callback?.invoke() }
    }

    fun setOnClickRight(callback: (() -> Unit)? = null) {
        actionRight.setOnSingClickListener { callback?.invoke() }
    }

    private fun initAttr(attr: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attr, R.styleable.ActionBarView)
        val title = typedArray.getString(R.styleable.ActionBarView_action_bar_title) ?: ""
        tvTitle.text = title
//        Action Left
        val srcLeft =
            typedArray.getResourceId(R.styleable.ActionBarView_action_bar_src_left, -1)

        if (srcLeft != -1) {
            actionLeft.setImageDrawable((ContextCompat.getDrawable(context, srcLeft)))
        }
        val enableLeft =
            typedArray.getBoolean(R.styleable.ActionBarView_action_bar_enable_left, false)
        actionLeft.visibility = if (enableLeft) View.VISIBLE else View.INVISIBLE

//        Action Right
        val srcRight = typedArray.getResourceId(R.styleable.ActionBarView_action_bar_src_right, -1)

        if (srcRight != -1)
            actionRight.setImageDrawable((ContextCompat.getDrawable(context, srcRight)))

        val enableRight =
            typedArray.getBoolean(R.styleable.ActionBarView_action_bar_enable_right, false)
        actionRight.visibility = if (enableRight) View.VISIBLE else View.INVISIBLE


        typedArray.recycle()
    }
}