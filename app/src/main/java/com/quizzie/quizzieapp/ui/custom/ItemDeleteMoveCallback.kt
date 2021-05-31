package com.quizzie.quizzieapp.ui.custom

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.quizzie.quizzieapp.R
import kotlin.math.abs

abstract class ItemDeleteMoveCallback(
    context: Context,
    private val isPressDragEnabled: Boolean,
    private val isSwipeEnabled: Boolean
): ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    private val deleteIcon = ContextCompat.getDrawable(context, R.drawable.ic_delete)
    private val intrinsicWidth = deleteIcon?.intrinsicWidth?.times(2)
    private val intrinsicHeight = deleteIcon?.intrinsicHeight
    private val background = ColorDrawable()
    private val backgroundColor = context.getColor(R.color.redColor)
    private val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }

    override fun isItemViewSwipeEnabled(): Boolean {
        return isSwipeEnabled
    }

    override fun isLongPressDragEnabled(): Boolean {
        return isPressDragEnabled
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top
        val itemWidth = itemView.right - itemView.left
        val isCanceled = dX == 0f && !isCurrentlyActive
        val minSwipeOffset = MIN_SWIPE_THRESHOLD * itemWidth

        if (isCanceled) {
            clearCanvas(c, itemView.right + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            return
        }

        // Draw the red delete background
        background.color = backgroundColor
        background.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
        background.draw(c)

        intrinsicWidth?.let {
            // Calculate position of delete icon
            val deleteIconTop = itemView.top + (itemHeight - it) / 2
            val deleteIconMargin = (itemHeight - it) / 2
            val deleteIconLeft = itemView.right - deleteIconMargin - it
            val deleteIconRight = itemView.right - deleteIconMargin
            val deleteIconBottom = deleteIconTop + it

            // Draw the delete icon
            deleteIcon?.alpha = (abs(dX) * 255/minSwipeOffset).toInt().let { value ->
                if (value >= 255) 255 else value
            }
            deleteIcon?.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
            deleteIcon?.draw(c)
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun clearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
        c?.drawRect(left, top, right, bottom, clearPaint)
    }

    companion object {
        const val MIN_SWIPE_THRESHOLD = 0.4
    }

}