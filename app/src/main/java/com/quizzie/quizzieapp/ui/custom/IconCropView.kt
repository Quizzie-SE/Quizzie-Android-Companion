//package com.quizzie.quizzieapp.ui.custom
//
//import android.annotation.SuppressLint
//import android.content.Context
//import android.graphics.Canvas
//import android.graphics.Color
//import android.graphics.Paint
//import android.graphics.Point
//import android.graphics.drawable.Drawable
//import android.util.AttributeSet
//import android.view.MotionEvent
//import android.view.View
//import com.quizzie.quizzieapp.R
//import kotlin.math.floor
//
//class IconCropView : View {
//    //drawing objects
//    private lateinit var paint: Paint
//
//    //point objects
//    private lateinit var points: Array<Point>
//    private lateinit var start: Point
//    private lateinit var offset: Point
//
//    //variable ints
//    private var minimumSideLength = 0
//    private var side = 0
//    private var halfCorner = 0
//    private var cornerColor = 0
//    private var edgeColor = 0
//    private var outsideColor = 0
//    private var corner = 5
//
//    //variable booleans
//    private var initialized = false
//
//    //drawables
//    private var moveDrawable: Drawable? = null
//    private var resizeDrawable1: Drawable? = null
//    private var resizeDrawable2: Drawable? = null
//    private var resizeDrawable3: Drawable? = null
//
//    //context
//    var mcontext: Context
//
//    constructor(context: Context) : super(context) {
//        this.mcontext = context
//        init(null)
//    }
//
//    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
//        this.mcontext = context
//        init(attrs)
//    }
//
//    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
//        context,
//        attrs,
//        defStyleAttr
//    ) {
//        this.mcontext = context
//        init(attrs)
//    }
//
//    constructor(
//        context: Context,
//        attrs: AttributeSet?,
//        defStyleAttr: Int,
//        defStyleRes: Int
//    ) : super(context, attrs, defStyleAttr, defStyleRes) {
//        this.mcontext = context
//        init(attrs)
//    }
//
//    private fun init(attrs: AttributeSet?) {
//        paint = Paint()
//        start = Point()
//        offset = Point()
//        val ta = mcontext.theme.obtainStyledAttributes(attrs, R.styleable.IconCropView, 0, 0)
//
//        //initial dimensions
//        minimumSideLength = ta.getDimensionPixelSize(R.styleable.IconCropView_minimumSide, 20)
//        side = minimumSideLength
//        halfCorner = ta.getDimensionPixelSize(R.styleable.IconCropView_cornerSize, 20) / 2
//
//        //colors
//        cornerColor = ta.getColor(R.styleable.IconCropView_cornerColor, Color.BLACK)
//        edgeColor = ta.getColor(R.styleable.IconCropView_edgeColor, Color.WHITE)
//        outsideColor =
//            ta.getColor(R.styleable.IconCropView_outsideCropColor, Color.parseColor("#00000088"))
//
//        //initialize corners;
//        points.fill(Point(), 0, 3)
//        points[0] = Point()
//        points[1] = Point()
//        points[2] = Point()
//        points[3] = Point()
//
//        //init corner locations;
//        //top left
//        points[0].x = 0
//        points[0].y = 0
//
//        //top right
//        points[1].x = minimumSideLength
//        points[1].y = 0
//
//        //bottom left
//        points[2].x = 0
//        points[2].y = minimumSideLength
//
//        //bottom right
//        points[3].x = minimumSideLength
//        points[3].y = minimumSideLength
//
//        //init drawables
//        moveDrawable = ta.getDrawable(R.styleable.IconCropView_moveCornerDrawable)
//        resizeDrawable1 = ta.getDrawable(R.styleable.IconCropView_resizeCornerDrawable)
//        resizeDrawable2 = ta.getDrawable(R.styleable.IconCropView_resizeCornerDrawable)
//        resizeDrawable3 = ta.getDrawable(R.styleable.IconCropView_resizeCornerDrawable)
//
//        //set drawable colors
//        moveDrawable?.setTint(cornerColor)
//        resizeDrawable1?.setTint(cornerColor)
//        resizeDrawable2?.setTint(cornerColor)
//        resizeDrawable3?.setTint(cornerColor)
//
//        //recycle attributes
//        ta.recycle()
//
//        //set initialized to true
//        initialized = true
//    }
//
//    override fun onDraw(canvas: Canvas) {
//        super.onDraw(canvas)
//        //set paint to draw edge, stroke
//        if (initialized) {
//            paint.isAntiAlias = true
//            paint.style = Paint.Style.STROKE
//            paint.strokeJoin = Paint.Join.ROUND
//            paint.color = edgeColor
//            paint.strokeWidth = 4f
//
//            //crop rectangle
//            canvas.drawRect(
//                (points[0]?.x?.plus(halfCorner))?.toFloat() ?: 0f,
//                (points[0]?.y?.plus(halfCorner))?.toFloat() ?: 0f,
//                (points[0]?.x?.plus(halfCorner) + side).toFloat(),
//                (points[0]?.y?.plus(halfCorner) + side).toFloat(),
//                paint
//            )
//
//            //set paint to draw outside color, fill
//            paint?.style = Paint.Style.FILL
//            paint?.color = outsideColor
//
//            //top rectangle
//            canvas.drawRect(
//                0f,
//                0f,
//                width.toFloat(),
//                (points[0].y + halfCorner).toFloat(),
//                paint
//            )
//            //left rectangle
//            canvas.drawRect(
//                0f,
//                (points[0].y + halfCorner).toFloat(),
//                (points[0].x + halfCorner).toFloat(),
//                height.toFloat(),
//                paint
//            )
//            //right rectangle
//            canvas.drawRect(
//                (points[0].x + halfCorner + side).toFloat(),
//                (points[0].y + halfCorner).toFloat(),
//                width.toFloat(),
//                (points[0].y + halfCorner + side).toFloat(),
//                paint
//            )
//            //bottom rectangle
//            canvas.drawRect(
//                (points[0].x + halfCorner).toFloat(),
//                (points[0]!!.y + halfCorner + side).toFloat(),
//                width.toFloat(),
//                height.toFloat(),
//                paint!!
//            )
//
//            //set bounds of drawables
//            moveDrawable?.setBounds(
//                points[0]!!.x,
//                points[0]!!.y,
//                points[0]!!.x + halfCorner * 2,
//                points[0]!!.y + halfCorner * 2
//            )
//            resizeDrawable1?.setBounds(
//                points[1]!!.x,
//                points[1]!!.y,
//                points[1]!!.x + halfCorner * 2,
//                points[1]!!.y + halfCorner * 2
//            )
//            resizeDrawable2?.setBounds(
//                points[2]!!.x,
//                points[2]!!.y,
//                points[2]!!.x + halfCorner * 2,
//                points[2].y + halfCorner * 2
//            )
//            resizeDrawable3?.setBounds(
//                points[3].x,
//                points[3].y,
//                points[3].x + halfCorner * 2,
//                points[3].y + halfCorner * 2
//            )
//
//            //place corner drawables
//            moveDrawable?.draw(canvas)
//            resizeDrawable1?.draw(canvas)
//            resizeDrawable2?.draw(canvas)
//            resizeDrawable3?.draw(canvas)
//        }
//    }
//
//    @SuppressLint("ClickableViewAccessibility")
//    override fun onTouchEvent(event: MotionEvent): Boolean {
//        //return super.onTouchEvent(event);
//        when (event.actionMasked) {
//            MotionEvent.ACTION_DOWN -> {
//
//
//                //get the coordinates
//                start.x = event.x.toInt()
//                start.y = event.y.toInt()
//
//                //get the corner touched if any
//                corner = getCorner(start.x.toFloat(), start.y.toFloat())
//
//                //get the offset of touch(x,y) from corner top-left point
//                offset = getOffset(start.x, start.y, corner)
//
//                //account for touch offset in starting point
//                start.x = start.x - offset.x
//                start.y = start.y - offset.y
//            }
//            MotionEvent.ACTION_UP -> {
//                run {}
//                run {
//                    if (corner == 0) {
//                        points[0].x = Math.max(
//                            points[0].x + Math.min(
//                                Math.floor((event.x - start.x - offset!!.x).toDouble()),
//                                Math.floor((width - points[0]!!.x - 2 * halfCorner - side).toDouble())
//                            ).toInt(), 0
//                        )
//                        points[1]!!.x = Math.max(
//                            points[1]!!.x + Math.min(
//                                Math.floor((event.x - start!!.x - offset!!.x).toDouble()),
//                                Math.floor((width - points[1]!!.x - 2 * halfCorner).toDouble())
//                            ).toInt(), side
//                        )
//                        points[2]!!.x = Math.max(
//                            points[2]!!.x + Math.min(
//                                Math.floor((event.x - start!!.x - offset!!.x).toDouble()),
//                                Math.floor((width - points[2]!!.x - 2 * halfCorner - side).toDouble())
//                            ).toInt(), 0
//                        )
//                        points[3]!!.x = Math.max(
//                            points[3]!!.x + Math.min(
//                                Math.floor((event.x - start!!.x - offset!!.x).toDouble()),
//                                Math.floor((width - points[3]!!.x - 2 * halfCorner).toDouble())
//                            ).toInt(), side
//                        )
//                        points[0]!!.y = Math.max(
//                            points[0]!!.y + Math.min(
//                                Math.floor((event.y - start!!.y - offset!!.y).toDouble()),
//                                Math.floor((height - points[0]!!.y - 2 * halfCorner - side).toDouble())
//                            ).toInt(), 0
//                        )
//                        points[1]!!.y = Math.max(
//                            points[1]!!.y + Math.min(
//                                Math.floor((event.y - start!!.y - offset!!.y).toDouble()),
//                                Math.floor((height - points[1]!!.y - 2 * halfCorner - side).toDouble())
//                            ).toInt(), 0
//                        )
//                        points[2]!!.y = Math.max(
//                            points[2]!!.y + Math.min(
//                                Math.floor((event.y - start!!.y - offset!!.y).toDouble()),
//                                Math.floor((height - points[2]!!.y - 2 * halfCorner).toDouble())
//                            ).toInt(), side
//                        )
//                        points[3]!!.y = Math.max(
//                            points[3]!!.y + Math.min(
//                                Math.floor((event.y - start!!.y - offset!!.y).toDouble()),
//                                Math.floor((height - points[3]!!.y - 2 * halfCorner).toDouble())
//                            ).toInt(), side
//                        )
//                        start!!.x = points[0]!!.x
//                        start!!.y = points[0]!!.y
//                        invalidate()
//                    } else if (corner == 1) {
//                        side = Math.min(
//                            Math.min(
//                                Math.max(
//                                    minimumSideLength,
//                                    (side + Math.floor(event.x.toDouble()) - start!!.x - offset!!.x).toInt()
//                                ), side + (width - points[1]!!.x - 2 * halfCorner)
//                            ), side + (height - points[2]!!.y - 2 * halfCorner)
//                        )
//                        points[1]!!.x = points[0]!!.x + side
//                        points[3]!!.x = points[0]!!.x + side
//                        points[3]!!.y = points[0]!!.y + side
//                        points[2]!!.y = points[0]!!.y + side
//                        start!!.x = points[1]!!.x
//                        invalidate()
//                    } else if (corner == 2) {
//                        side = Math.min(
//                            Math.min(
//                                Math.max(
//                                    minimumSideLength,
//                                    (side + Math.floor(event.y.toDouble()) - start!!.y - offset!!.y).toInt()
//                                ), side + (height - points[2]!!.y - 2 * halfCorner)
//                            ), side + (width - points[1]!!.x - 2 * halfCorner)
//                        )
//                        points[2]!!.y = points[0]!!.y + side
//                        points[3]!!.y = points[0]!!.y + side
//                        points[3]!!.x = points[0]!!.x + side
//                        points[1]!!.x = points[0]!!.x + side
//                        start!!.y = points[2]!!.y
//                        invalidate()
//                    } else if (corner == 3) {
//                        side = Math.min(
//                            Math.min(
//                                Math.min(
//                                    Math.max(
//                                        minimumSideLength,
//                                        (side + Math.floor(event.x.toDouble()) - start!!.x - offset!!.x).toInt()
//                                    ), side + (width - points[3]!!.x - 2 * halfCorner)
//                                ), side + (height - points[3]!!.y - 2 * halfCorner)
//                            ), Math.min(
//                                Math.min(
//                                    Math.max(
//                                        minimumSideLength,
//                                        (side + Math.floor(event.y.toDouble()) - start!!.y - offset!!.y).toInt()
//                                    ), side + (height - points[3]!!.y - 2 * halfCorner)
//                                ), side + (width - points[3]!!.x - 2 * halfCorner)
//                            )
//                        )
//                        points[1]!!.x = points[0]!!.x + side
//                        points[3]!!.x = points[0]!!.x + side
//                        points[3]!!.y = points[0]!!.y + side
//                        points[2]!!.y = points[0]!!.y + side
//                        start!!.x = points[3]!!.x
//                        points[2]!!.y = points[0]!!.y + side
//                        points[3]!!.y = points[0]!!.y + side
//                        points[3]!!.x = points[0]!!.x + side
//                        points[1]!!.x = points[0]!!.x + side
//                        start!!.y = points[3]!!.y
//                        invalidate()
//                    }
//                }
//            }
//            MotionEvent.ACTION_MOVE -> {
//                if (corner == 0) {
//                    points[0]!!.x = Math.max(
//                        points[0]!!.x + Math.min(
//                            Math.floor((event.x - start!!.x - offset!!.x).toDouble()),
//                            Math.floor((width - points[0]!!.x - 2 * halfCorner - side).toDouble())
//                        ).toInt(), 0
//                    )
//                    points[1]!!.x = Math.max(
//                        points[1]!!.x + Math.min(
//                            Math.floor((event.x - start!!.x - offset!!.x).toDouble()),
//                            Math.floor((width - points[1]!!.x - 2 * halfCorner).toDouble())
//                        ).toInt(), side
//                    )
//                    points[2]!!.x = Math.max(
//                        points[2]!!.x + Math.min(
//                            floor((event.x - start!!.x - offset!!.x).toDouble()),
//                            floor((width - points[2]!!.x - 2 * halfCorner - side).toDouble())
//                        ).toInt(), 0
//                    )
//                    points[3]!!.x = Math.max(
//                        points[3]!!.x + Math.min(
//                            Math.floor((event.x - start!!.x - offset!!.x).toDouble()),
//                            Math.floor((width - points[3]!!.x - 2 * halfCorner).toDouble())
//                        ).toInt(), side
//                    )
//                    points[0]!!.y = Math.max(
//                        points[0]!!.y + Math.min(
//                            Math.floor((event.y - start!!.y - offset!!.y).toDouble()),
//                            Math.floor((height - points[0]!!.y - 2 * halfCorner - side).toDouble())
//                        ).toInt(), 0
//                    )
//                    points[1]!!.y = Math.max(
//                        points[1]!!.y + Math.min(
//                            Math.floor((event.y - start!!.y - offset!!.y).toDouble()),
//                            Math.floor((height - points[1]!!.y - 2 * halfCorner - side).toDouble())
//                        ).toInt(), 0
//                    )
//                    points[2]!!.y = Math.max(
//                        points[2]!!.y + Math.min(
//                            floor((event.y - start!!.y - offset!!.y).toDouble()),
//                            floor((height - points[2]!!.y - 2 * halfCorner).toDouble())
//                        ).toInt(), side
//                    )
//                    points[3]!!.y = Math.max(
//                        points[3]!!.y + Math.min(
//                            Math.floor((event.y - start!!.y - offset!!.y).toDouble()),
//                            Math.floor((height - points[3]!!.y - 2 * halfCorner).toDouble())
//                        ).toInt(), side
//                    )
//                    start!!.x = points[0]!!.x
//                    start!!.y = points[0]!!.y
//                    invalidate()
//                } else if (corner == 1) {
//                    side = Math.min(
//                        Math.min(
//                            Math.max(
//                                minimumSideLength,
//                                (side + Math.floor(event.x.toDouble()) - start!!.x - offset!!.x).toInt()
//                            ), side + (width - points[1]!!.x - 2 * halfCorner)
//                        ), side + (height - points[2]!!.y - 2 * halfCorner)
//                    )
//                    points[1]!!.x = points[0]!!.x + side
//                    points[3]!!.x = points[0]!!.x + side
//                    points[3]!!.y = points[0]!!.y + side
//                    points[2]!!.y = points[0]!!.y + side
//                    start!!.x = points[1]!!.x
//                    invalidate()
//                } else if (corner == 2) {
//                    side = Math.min(
//                        Math.min(
//                            Math.max(
//                                minimumSideLength,
//                                (side + Math.floor(event.y.toDouble()) - start!!.y - offset!!.y).toInt()
//                            ), side + (height - points[2]!!.y - 2 * halfCorner)
//                        ), side + (width - points[1]!!.x - 2 * halfCorner)
//                    )
//                    points[2]!!.y = points[0]!!.y + side
//                    points[3]!!.y = points[0]!!.y + side
//                    points[3]!!.x = points[0]!!.x + side
//                    points[1]!!.x = points[0]!!.x + side
//                    start!!.y = points[2]!!.y
//                    invalidate()
//                } else if (corner == 3) {
//                    side = Math.min(
//                        Math.min(
//                            Math.min(
//                                Math.max(
//                                    minimumSideLength,
//                                    (side + Math.floor(event.x.toDouble()) - start!!.x - offset!!.x).toInt()
//                                ), side + (width - points[3]!!.x - 2 * halfCorner)
//                            ), side + (height - points[3]!!.y - 2 * halfCorner)
//                        ), Math.min(
//                            Math.min(
//                                Math.max(
//                                    minimumSideLength,
//                                    (side + Math.floor(event.y.toDouble()) - start!!.y - offset!!.y).toInt()
//                                ), side + (height - points[3]!!.y - 2 * halfCorner)
//                            ), side + (width - points[3]!!.x - 2 * halfCorner)
//                        )
//                    )
//                    points[1]!!.x = points[0]!!.x + side
//                    points[3]!!.x = points[0]!!.x + side
//                    points[3]!!.y = points[0]!!.y + side
//                    points[2]!!.y = points[0]!!.y + side
//                    start!!.x = points[3]!!.x
//                    points[2]!!.y = points[0]!!.y + side
//                    points[3]!!.y = points[0]!!.y + side
//                    points[3]!!.x = points[0]!!.x + side
//                    points[1]!!.x = points[0]!!.x + side
//                    start!!.y = points[3]!!.y
//                    invalidate()
//                }
//            }
//        }
//        return true
//    }
//
//    private fun getCorner(x: Float, y: Float): Int {
//        val corner = 5
//        for (i in points.indices) {
//            val dx = x - points[i]!!.x
//            val dy = y - points[i]!!.y
//            val max = halfCorner * 2
//            if (dx <= max && dx >= 0 && dy <= max && dy >= 0) {
//                return i
//            }
//        }
//        return corner
//    }
//
//    private fun getOffset(left: Int, top: Int, corner: Int): Point {
//        val offset = Point()
//        if (corner == 5) {
//            offset.x = 0
//            offset.y = 0
//        } else {
//            offset.x = left - points[corner]!!.x
//            offset.y = top - points[corner]!!.y
//        }
//        return offset
//    }
//
//    companion object {
//        //contants strings
//        private const val TAG = "IconCropView"
//    }
//}