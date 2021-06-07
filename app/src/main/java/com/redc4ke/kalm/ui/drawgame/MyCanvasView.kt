package com.redc4ke.kalm.ui.drawgame

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.core.content.res.ResourcesCompat
import com.redc4ke.kalm.R
import kotlin.math.abs

private const val STROKE_WIDTH = 12f
private const val ERASER_WIDTH = 50f

class MyCanvasView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : this(context, attrs)

    private lateinit var extraCanvas: Canvas
    private lateinit var extraBitmap: Bitmap
    private val backgroundColor =
        ResourcesCompat.getColor(resources, R.color.transparent, null)
    private var drawColor =
        ResourcesCompat.getColor(resources, R.color.black, null)
    private val drawingPaint = Paint().apply {
        color = drawColor
        isAntiAlias = true
        isDither = true
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = STROKE_WIDTH
    }
    private val eraser = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.TRANSPARENT
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        style = Paint.Style.STROKE
        strokeWidth = ERASER_WIDTH
        isAntiAlias = true
    }
    private var currentPaint = drawingPaint
    private var path = Path()
    private var motionTouchEventX = 0f
    private var motionTouchEventY = 0f
    private var currentX = 0f
    private var currentY = 0f
    private val touchTolerance = ViewConfiguration.get(context).scaledTouchSlop


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        extraBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        extraCanvas= Canvas(extraBitmap)
        extraCanvas.drawColor(backgroundColor)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawBitmap(extraBitmap, 0f , 0f, null)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        motionTouchEventX = event?.x ?: 0f
        motionTouchEventY = event?.y ?: 0f

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> touchStart()
            MotionEvent.ACTION_MOVE -> touchMove()
            MotionEvent.ACTION_UP -> touchUp()
        }

        return true
    }

    private fun touchStart() {
        path.reset()
        path.moveTo(motionTouchEventX, motionTouchEventY)
        currentX = motionTouchEventX
        currentY = motionTouchEventY
    }

    private fun touchMove() {
        val dx = abs(motionTouchEventX - currentX)
        val dy = abs(motionTouchEventY - currentY)

        if (dx >= touchTolerance || dy >= touchTolerance) {
            path.quadTo(currentX, currentY,
                (motionTouchEventX + currentX) /2, (motionTouchEventY + currentY) / 2)
            currentX = motionTouchEventX
            currentY = motionTouchEventY
            extraCanvas.drawPath(path, currentPaint)
        }
        invalidate()
    }

    private fun touchUp() {
        path.reset()
    }

    fun changePaint(color: String) {
        when (color) {
            "eraser" -> {
                currentPaint = eraser
            }
            "green" -> {
                currentPaint = drawingPaint
                currentPaint.color =
                    ResourcesCompat.getColor(resources, R.color.secondary, null)
            }
            "blue" -> {
                currentPaint = drawingPaint
                currentPaint.color =
                    ResourcesCompat.getColor(resources, R.color.primary, null)
            }
            "black" -> {
                currentPaint = drawingPaint
                currentPaint.color =
                    ResourcesCompat.getColor(resources, R.color.black, null)
            }
        }
    }

}