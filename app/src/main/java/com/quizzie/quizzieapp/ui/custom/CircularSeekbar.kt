package com.quizzie.quizzieapp.ui.custom

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.quizzie.quizzieapp.R
import timber.log.Timber
import kotlin.math.*

class CircularSeekbar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    /** The context  */
    private var mContext: Context = context
    /**
     * Gets the seek bar change listener.
     *
     * @return the seek bar change listener
     */
    /**
     * Sets the seek bar change listener.
     *
     * @param listener
     * the new seek bar change listener
     */
    /** The listener to listen for changes  */
    var seekBarChangeListener: OnSeekChangeListener? = null

    /** The color of the progress ring  */
    private var circleColor = Paint()

    /** the color of the inside circle. Acts as background color  */
    private var innerColor = Paint()

    /** The progress circle ring background  */
    private var circleRing = Paint()

    /** The angle of progress  */
    private var angle = 30F

    /** The start angle (12 O'clock  */
    private val startAngle = 270F
    /**
     * Gets the bar width.
     *
     * @return the bar width
     */
    /**
     * Sets the bar width.
     *
     * @param barWidth
     * the new bar width
     */
    /** The width of the progress ring  */
    var barWidth = 5F

    /**
     * Gets the max progress.
     *
     * @return the max progress
     */
    /**
     * Sets the max progress.
     *
     * @param maxProgress
     * the new max progress
     */
    /** The maximum progress amount  */
    var maxProgress = 100F
    /**
     * Gets the progress.
     *
     * @return the progress
     */
    /**
     * Sets the progress.
     *
     * @param progress
     * the new progress
     */
    /** The current progress  */
     var progress = 50F
        set(progress) {
            if (this.progress != progress) {
                field = progress
                if (!CALLED_FROM_ANGLE) {
                    val newPercent = this.progress * 100 / maxProgress
                    val newAngle = newPercent * 360F / 100
                    setAngle(newAngle)
                    progressPercent = newPercent
                }
                seekBarChangeListener?.onProgressChange(this, progress.toInt())
                CALLED_FROM_ANGLE = false
                invalidate()
            }
        }
    /**
     * Gets the progress percent.
     *
     * @return the progress percent
     */
    /**
     * Sets the progress percent.
     *
     * @param progressPercent
     * the new progress percent
     */
    /** The progress percent  */
    private var progressPercent = 0F

    /** The radius of the inner circle  */
    private var innerRadius = 0f

    /** The radius of the outer circle  */
    private var outerRadius = 0f

    /** The circle's center X coordinate  */
    private var cx = 0f

    /** The circle's center Y coordinate  */
    private var cy = 0f

    /** The left bound for the circle RectF  */
    private var left = 0f

    /** The right bound for the circle RectF  */
    private var right = 0f

    /** The top bound for the circle RectF  */
    private var top = 0f

    /** The bottom bound for the circle RectF  */
    private var bottom = 0f

    /** The X coordinate for the top left corner of the marking drawable  */
    private var dx = 0f

    /** The Y coordinate for the top left corner of the marking drawable  */
    private var dy = 0f

    /** The X coordinate for 12 O'Clock  */
    private var startPointX = 0f

    /** The Y coordinate for 12 O'Clock  */
    private var startPointY = 0f

    /**
     * The X coordinate for the current position of the marker, pre adjustment
     * to center
     */
    private var markPointX = 0f

    /**
     * The Y coordinate for the current position of the marker, pre adjustment
     * to center
     */
    private var markPointY = 0f
    /**
     * Gets the adjustment factor.
     *
     * @return the adjustment factor
     */
    /**
     * Sets the adjustment factor.
     *
     * @param adjustmentFactor
     * the new adjustment factor
     */
    /**
     * The adjustment factor. This adds an adjustment of the specified size to
     * both sides of the progress bar, allowing touch events to be processed
     * more user friendly (yes, I know that's not a word)
     */
    private var adjustmentFactor = 100f

    /** The progress mark when the view isn't being progress modified  */
    private var progressMark: Bitmap? = null

    /** The progress mark when the view is being progress modified.  */
    private var progressMarkPressed: Bitmap? = null

    /** The flag to see if view is pressed  */
    private var IS_PRESSED = false

    /**
     * The flag to see if the setProgress() method was called from our own
     * View's setAngle() method, or externally by a user.
     */
    private var CALLED_FROM_ANGLE = false
    private var SHOW_SEEKBAR = true

    /** The rectangle containing our circles and arcs.  */
    private val rect = RectF()

    init {
        seekBarChangeListener = object : OnSeekChangeListener {
            override fun onProgressChange(view: CircularSeekbar?, newProgress: Int) {}
        }

        circleColor.color = Color.parseColor("#ff33b5e5") // Set default
        // progress
        // color to holo
        // blue.
        innerColor?.color = Color.BLACK // Set default background color to
        // black
        circleRing?.color = Color.GRAY // Set default background color to Gray
        circleColor?.isAntiAlias = true
        innerColor?.isAntiAlias = true
        circleRing?.isAntiAlias = true
        circleColor?.strokeWidth = 5F
        innerColor?.strokeWidth = 5F
        circleRing?.strokeWidth = 5F
        circleColor?.style = Paint.Style.FILL

        initDrawable()
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CircularSeekbar,
            0, 0
        ).apply {

            try {
                progress = getFloat(R.styleable.CircularSeekbar_progress, 0F)
                maxProgress = getFloat(R.styleable.CircularSeekbar_maxProgress, 100F)
                circleRing?.color = getColor(
                    R.styleable.CircularSeekbar_progressBackgroundTint,
                    context.getColor(android.R.color.transparent)
                )

                circleColor?.color = getColor(R.styleable.CircularSeekbar_progressTint,
                    with(TypedValue()) {
                        context.theme.resolveAttribute(
                            R.attr.colorPrimary,
                            this,
                            true
                        ); data
                    })

                innerColor?.color = getColor(
                    R.styleable.CircularSeekbar_backgroundColor,
                    context.getColor(android.R.color.transparent)
                )

                barWidth = getDimension(R.styleable.CircularSeekbar_thickness, 5F)

            } finally {
                recycle()
            }
        }
    }

    /**
     * Inits the drawable.
     */
    private fun initDrawable() {
        progressMark = BitmapFactory.decodeResource(
            mContext.resources,
            R.drawable.ic_circle
        )
        progressMarkPressed = BitmapFactory.decodeResource(
            mContext.resources,
            R.drawable.ic_circle
        )
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val size = if (width > height) height else width // Choose the smaller
        // between width and
        // height to make a
        // square
        
        cx = (width / 2).toFloat() // Center X for circle
        cy = (height / 2).toFloat() // Center Y for circle
        outerRadius = (size / 2).toFloat() // Radius of the outer circle
        innerRadius = outerRadius - barWidth // Radius of the inner circle
        left = cx - outerRadius // Calculate left bound of our rect
        right = cx + outerRadius // Calculate right bound of our rect
        top = cy - outerRadius // Calculate top bound of our rect
        bottom = cy + outerRadius // Calculate bottom bound of our rect
        startPointX = cx // 12 O'clock X coordinate
        startPointY = cy - outerRadius // 12 O'clock Y coordinate
        markPointX = startPointX // Initial locatino of the marker X coordinate
        markPointY = startPointY // Initial locatino of the marker Y coordinate
        rect[left, top, right] = bottom // assign size to rect
    }

    /*
 * (non-Javadoc)
 *
 * @see android.view.View#onDraw(android.graphics.Canvas)
 */
    override fun onDraw(canvas: Canvas) {
        Timber.d("$outerRadius, $innerRadius, $progress, $angle")
        canvas.drawCircle(cx, cy, outerRadius, circleRing)
        canvas.drawArc(rect, startAngle, angle, true, circleColor)
        canvas.drawCircle(cx, cy, innerRadius, innerColor)

        if (SHOW_SEEKBAR) {
            dx = xFromAngle
            dy = yFromAngle
            drawMarkerAtProgress(canvas)
        }
        super.onDraw(canvas)
    }

    /**
     * Draw marker at the current progress point onto the given canvas.
     *
     * @param canvas
     * the canvas
     */
    private fun drawMarkerAtProgress(canvas: Canvas) {
        if (IS_PRESSED) {
            progressMarkPressed?.let { canvas.drawBitmap(it, dx, dy, null) }
        } else {
            progressMark?.let { canvas.drawBitmap(it, dx, dy, null) }
        }
    }

    /**
     * Gets the X coordinate of the arc's end arm's point of intersection with
     * the circle
     *
     * @return the X coordinate
     */
    private val xFromAngle: Float
        get() {
            val size1 = progressMark?.width ?: 0
            val size2 = progressMarkPressed?.width ?: 0
            val adjust = if (size1 > size2) size1 else size2
            return markPointX - adjust / 2
        }

    /**
     * Gets the Y coordinate of the arc's end arm's point of intersection with
     * the circle
     *
     * @return the Y coordinate
     */
    private val yFromAngle: Float
        get() {
            val size1 = progressMark?.height ?: 0
            val size2 = progressMarkPressed?.height ?: 0
            val adjust = if (size1 > size2) size1 else size2
            return markPointY - adjust / 2
        }

    /**
     * Set the angle.
     *
     * @param angle
     * the new angle
     */
    private fun setAngle(angle: Float) {
        this.angle = angle
        val donePercent = this.angle / 360 * 100
        val progress = donePercent / 100 * maxProgress
        progressPercent = donePercent
        CALLED_FROM_ANGLE = true
        this.progress = progress.roundToInt().toFloat()
    }

    /**
     * The listener interface for receiving onSeekChange events. The class that
     * is interested in processing a onSeekChange event implements this
     * interface, and the object created with that class is registered with a
     * component using the component's
     * `setSeekBarChangeListener(OnSeekChangeListener)` method. When
     * the onSeekChange event occurs, that object's appropriate
     * method is invoked.
     *
     * @see OnSeekChangeEvent
    `` */
    interface OnSeekChangeListener {
        /**
         * On progress change.
         *
         * @param view
         * the view
         * @param newProgress
         * the new progress
         */
        fun onProgressChange(view: CircularSeekbar?, newProgress: Int)
    }

    /**
     * Sets the ring background color.
     *
     * @param color
     * the new ring background color
     */
    fun setRingBackgroundColor(color: Int) {
        circleRing?.color = color
    }

    /**
     * Sets the back ground color.
     *
     * @param color
     * the new back ground color
     */
    fun setBackGroundColor(color: Int) {
        innerColor?.color = color
    }

    /**
     * Sets the progress color.
     *
     * @param color
     * the new progress color
     */
    fun setProgressColor(color: Int) {
        circleColor?.color = color
    }

    /*
 * (non-Javadoc)
 *
 * @see android.view.View#onTouchEvent(android.view.MotionEvent)
 */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        var up = false
        when (event.action) {
            MotionEvent.ACTION_DOWN -> moved(x, y, up)
            MotionEvent.ACTION_MOVE -> moved(x, y, up)
            MotionEvent.ACTION_UP -> {
                up = true
                moved(x, y, up)
            }
        }
        return true
    }

    /**
     * Moved.
     *
     * @param x
     * the x
     * @param y
     * the y
     * @param up
     * the up
     */
    private fun moved(x: Float, y: Float, up: Boolean) {
        val distance =
            sqrt((x - cx).toDouble().pow(2.0) + (y - cy).toDouble().pow(2.0))
                .toFloat()
        if (distance < outerRadius + adjustmentFactor && distance > innerRadius - adjustmentFactor && !up) {
            IS_PRESSED = true
            markPointX =
                (cx + outerRadius * cos(
                    atan2(
                        (x - cx).toDouble(),
                        (cy - y).toDouble()
                    ) - Math.PI / 2
                )).toFloat()
            markPointY =
                (cy + outerRadius * sin(
                    atan2(
                        (x - cx).toDouble(),
                        (cy - y).toDouble()
                    ) - Math.PI / 2
                )).toFloat()
            var degrees =
                ((Math.toDegrees(
                    atan2(
                        (x - cx).toDouble(),
                        (cy - y).toDouble()
                    )
                ) + 360.0).toFloat() % 360.0).toFloat()
            // and to make it count 0-360
            if (degrees < 0) {
                degrees += (2 * Math.PI).toFloat()
            }
            setAngle(degrees)
            invalidate()
        } else {
            IS_PRESSED = false
            invalidate()
        }
    }

    companion object {
        @BindingAdapter("progressAttrChanged")
        @JvmStatic fun setProgressChangeListener(
            view: CircularSeekbar,
            attrChange: InverseBindingListener
        ) {
            view.seekBarChangeListener = object : OnSeekChangeListener{
                override fun onProgressChange(view: CircularSeekbar?, newProgress: Int) {
                    attrChange.onChange()
                }
            }
        }


        @BindingAdapter("progress")
        @JvmStatic fun setProgress(view: CircularSeekbar, progress: Float) {
            view.progress = progress
        }

        @InverseBindingAdapter(attribute = "progress")
        @JvmStatic fun getProgress(view: CircularSeekbar) = view.progress

    }

}
