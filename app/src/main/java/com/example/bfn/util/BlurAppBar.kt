package com.example.bfn.util

import android.content.Context
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.appbar.AppBarLayout
import jp.wasabeef.blurry.Blurry

class BlurAppBar(val context: Context) {


    var blurred = false
    fun blurAppBar(appBar: AppBarLayout, blurro: ConstraintLayout) {


        appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { p0, p1 ->
            val alpha = (p0.totalScrollRange + p1).toFloat() / p0.totalScrollRange
            if ((alpha == 0f || alpha == 1f)) {
                Blurry.delete(blurro as ViewGroup)
                blurred = false
            } else if ((alpha > 0 && alpha < 1) && !blurred) {
                blurred = true
                Blurry.with(context)
                    .radius(25)
                    .sampling(2)
                    .async()
                    .animate(125)
                    .onto(blurro as ViewGroup)

            }

        })
    }
}