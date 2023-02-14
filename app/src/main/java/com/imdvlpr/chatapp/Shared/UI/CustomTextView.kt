package com.imdvlpr.chatapp.Shared.UI

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.imdvlpr.chatapp.R
import com.imdvlpr.chatapp.Shared.Extension.setVisible
import com.imdvlpr.chatapp.databinding.LayoutTextviewBinding

class CustomTextView: ConstraintLayout {

    private lateinit var mContext: Context
    private lateinit var binding: LayoutTextviewBinding
    private var listener: TextViewListener? = null

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    /**
     * Initialization
     */
    private fun init(context: Context, attributeSet: AttributeSet?) {
        mContext = context
        binding = LayoutTextviewBinding.bind(LayoutInflater.from(mContext).inflate(R.layout.layout_textview, this, true))

        binding.btnAction.setOnClickListener { listener?.onActionClickListener() }
    }

    fun setTitle(isVisible: Boolean, title: String) {
        binding.lblTitle.setVisible(isVisible)
        binding.lblTitle.text = title
    }

    fun setValue(value: String) {
        binding.txtValue.text = value
    }

    fun setTextViewIcon(isVisible: Boolean, drawable: Int) {
        binding.iconLabel.setVisible(isVisible)
        binding.iconLabel.setImageDrawable(ContextCompat.getDrawable(mContext, drawable))
    }

    fun setIconAction(isVisible: Boolean, drawable: Int) {
        binding.btnAction.setVisible(isVisible)
        binding.btnAction.setImageDrawable(ContextCompat.getDrawable(mContext, drawable))
    }

    fun setIconColor(color: Int) {
        binding.iconLabel.setColorFilter(ContextCompat.getColor(mContext, color))
        binding.btnAction.setColorFilter(ContextCompat.getColor(mContext, color))
    }

    fun setListener(textViewListener: TextViewListener) {
        listener = textViewListener
    }

    interface TextViewListener {
        fun onActionClickListener()
    }
}