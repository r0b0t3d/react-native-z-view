package com.reactnativezview

import android.content.Context
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.ReadableType
import com.facebook.react.uimanager.PixelUtil.toPixelFromDIP
import com.facebook.react.views.view.ReactViewGroup

class ZView(context: Context) : ReactViewGroup(context) {
  private var mHostView: ZViewRootViewGroup? = null

  private val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

  private var touchable = true

  private var coords: ReadableMap? = null

  override fun addView(child: View, index: Int, params: LayoutParams) {
    mHostView = child as ZViewRootViewGroup
    val windowParams = WindowManager.LayoutParams(
      WindowManager.LayoutParams.WRAP_CONTENT,
      WindowManager.LayoutParams.WRAP_CONTENT,
      WindowManager.LayoutParams.TYPE_APPLICATION,
      (WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
        or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN),
      PixelFormat.TRANSLUCENT
    )

    windowParams.gravity = Gravity.TOP or Gravity.LEFT
    windowParams.x = 0
    windowParams.y = 0
    windowManager.addView(mHostView, windowParams)
    updateTouchable()
    updateXAndY()
  }

  private fun updateTouchable() {
    if (this.mHostView != null) {
      val existingParams = mHostView!!.getLayoutParams() as WindowManager.LayoutParams
      if (touchable) {
        existingParams.flags =
          existingParams.flags and WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE.inv()
      } else {
        existingParams.flags = existingParams.flags or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
      }
      windowManager.updateViewLayout(this.mHostView, existingParams)
    }
  }

  override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
    super.onLayout(changed, left, top, right, bottom)
    val width = right - left
    val height = bottom - top
    if (mHostView != null) {
      val existingParams = mHostView!!.getLayoutParams() as WindowManager.LayoutParams
      existingParams.width = width
      existingParams.height = height
      windowManager.updateViewLayout(mHostView, existingParams)
      updateXAndY()
    }
  }


  override fun removeView(child: View) {
    // ZView will always have a single child
    mHostView = null
  }

  override fun removeViewAt(index: Int) {
    // ZView will always have a single child
    mHostView = null
  }

  fun setCoords(coords: ReadableMap?) {
    this.coords = coords
    updateXAndY()
  }

  fun setTouchable(touchable: Boolean) {
    this.touchable = touchable
    updateTouchable()
  }

  private fun updateXAndY() {
    if (mHostView != null) {
      val decorWidth: Int = mHostView!!.getWidth()
      val decorHeight: Int = mHostView!!.getHeight()
      val displayMetrics = context.resources.displayMetrics
      val screenWidth = displayMetrics.widthPixels
      val screenHeight = displayMetrics.heightPixels
      var top = 0
      var left = 0
      if (coords != null) {
        if (coords!!.hasKey("top")) {
          val topDynamic = coords!!.getDynamic("top")
          if (topDynamic.type == ReadableType.Number) {
            top = toPixelFromDIP(coords!!.getInt("top").toFloat()).toInt()
          } else if (topDynamic.type == ReadableType.String && topDynamic.asString()
              .endsWith("%")
          ) {
            val topPer = topDynamic.asString().replace("%", "").toInt()
            top = ((topPer / 100.0) * screenHeight).toInt()
          }
        } else if (coords!!.hasKey("bottom")) {
          val bottomDynamic = coords!!.getDynamic("bottom")
          if (bottomDynamic.type == ReadableType.Number) {
            top = screenHeight - decorHeight - toPixelFromDIP(
              coords!!.getInt("bottom").toFloat()
            ).toInt()
          } else if (bottomDynamic.type == ReadableType.String && bottomDynamic.asString()
              .endsWith("%")
          ) {
            val bottomPer = bottomDynamic.asString().replace("%", "").toInt()
            top = screenHeight - (bottomPer / 100.0 * screenHeight).toInt() - decorHeight
          }
        }

        if (coords!!.hasKey("left")) {
          val leftDynamic = coords!!.getDynamic("left")
          if (leftDynamic.type == ReadableType.Number) {
            left = toPixelFromDIP(coords!!.getInt("left").toFloat()).toInt()
          } else if (leftDynamic.type == ReadableType.String && leftDynamic.asString()
              .endsWith("%")
          ) {
            val leftPer = leftDynamic.asString().replace("%", "").toInt()
            left = ((leftPer / 100.0) * screenWidth).toInt()
          }
        } else if (coords!!.hasKey("right")) {
          val rightDynamic = coords!!.getDynamic("right")
          if (rightDynamic.type == ReadableType.Number) {
            left =
              screenWidth - decorWidth - toPixelFromDIP(coords!!.getInt("right").toFloat()).toInt()
          } else if (rightDynamic.type == ReadableType.String && rightDynamic.asString()
              .endsWith("%")
          ) {
            val rightPer = rightDynamic.asString().replace("%", "").toInt()
            left = screenWidth - (rightPer / 100.0 * screenWidth).toInt() - decorWidth
          }
        }
      }

      val existingParams = mHostView!!.getLayoutParams() as WindowManager.LayoutParams
      existingParams.x = left
      existingParams.y = top
      windowManager.updateViewLayout(mHostView, existingParams)
    }
  }

  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    if (mHostView != null) {
      windowManager.removeView(this.mHostView)
      mHostView = null
    }
  }
}
