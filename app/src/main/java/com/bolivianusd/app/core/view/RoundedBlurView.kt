package com.bolivianusd.app.core.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import com.bolivianusd.app.R
import eightbitlab.com.blurview.BlurView
import androidx.core.content.withStyledAttributes

class RoundedBlurView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : BlurView(context, attrs, defStyle) {
    private var cornerRadiusTopLeft: Float = 0f
    private var cornerRadiusTopRight: Float = 0f
    private var cornerRadiusBottomLeft: Float = 0f
    private var cornerRadiusBottomRight: Float = 0f

    init {
        context.withStyledAttributes(attrs, R.styleable.RoundedBlurView) {
            cornerRadiusTopLeft = getDimension(R.styleable.RoundedBlurView_cornerRadiusTopLeft, 0f)
            cornerRadiusTopRight =
                getDimension(R.styleable.RoundedBlurView_cornerRadiusTopRight, 0f)
            cornerRadiusBottomLeft =
                getDimension(R.styleable.RoundedBlurView_cornerRadiusBottomLeft, 0f)
            cornerRadiusBottomRight =
                getDimension(R.styleable.RoundedBlurView_cornerRadiusBottomRight, 0f)
        }
    }
    override fun draw(canvas: Canvas) {
        val path = Path()
        val radii = floatArrayOf(
            cornerRadiusTopLeft, cornerRadiusTopLeft,
            cornerRadiusTopRight, cornerRadiusTopRight,
            cornerRadiusBottomLeft, cornerRadiusBottomLeft,
            cornerRadiusBottomRight, cornerRadiusBottomRight
        )
        path.addRoundRect(
            RectF(0f, 0f, width.toFloat(), height.toFloat()),
            radii,
            Path.Direction.CW
        )
        canvas.clipPath(path)

        super.draw(canvas)
    }
}
