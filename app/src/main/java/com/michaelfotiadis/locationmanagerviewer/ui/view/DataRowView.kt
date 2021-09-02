package com.michaelfotiadis.locationmanagerviewer.ui.view

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.michaelfotiadis.locationmanagerviewer.R
import com.michaelfotiadis.locationmanagerviewer.databinding.ViewDataRowBinding

class DataRowView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private lateinit var binding: ViewDataRowBinding

    init {
        inflate(getContext(), R.layout.view_data_row, this)
    }

    fun setTitle(@StringRes resId: Int) {
        binding.dataTitle.setText(resId)
    }

    fun setData(@StringRes resId: Int) {
        binding.dataContent.setText(resId)
    }


}