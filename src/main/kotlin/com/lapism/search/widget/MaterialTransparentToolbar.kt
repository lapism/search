package com.lapism.search.widget

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.google.android.material.appbar.MaterialToolbar


open class MaterialTransparentToolbar : MaterialToolbar {

    // *********************************************************************************************
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    // *********************************************************************************************
    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            outlineAmbientShadowColor = ContextCompat.getColor(context, android.R.color.transparent)
            outlineSpotShadowColor = ContextCompat.getColor(context, android.R.color.transparent)
        }
    }

}