package com.michaelfotiadis.locationmanagerviewer.ui.view

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.michaelfotiadis.locationmanagerviewer.R
import com.michaelfotiadis.locationmanagerviewer.databinding.ViewHeaderRowBinding

class HeaderRowView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private lateinit var binding: ViewHeaderRowBinding

    init {
        inflate(getContext(), R.layout.view_header_row, this)
    }

    fun setContent(@StringRes resId: Int) {
        binding.headerText.setText(resId)
    }


}