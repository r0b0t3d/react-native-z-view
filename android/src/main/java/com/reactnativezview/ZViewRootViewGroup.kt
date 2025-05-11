package com.reactnativezview

import android.content.Context
import android.view.MotionEvent
import android.view.View
import com.facebook.react.uimanager.JSTouchDispatcher
import com.facebook.react.uimanager.RootView
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.events.EventDispatcher
import com.facebook.react.views.view.ReactViewGroup


class ZViewRootViewGroup(context: Context?) : ReactViewGroup(context), RootView {
  private var mEventDispatcher: EventDispatcher? = null
  private val mJSTouchDispatcher = JSTouchDispatcher(this)

  fun setEventDispatcher(eventDispatcher: EventDispatcher?) {
    this.mEventDispatcher = eventDispatcher
  }

  override fun handleException(t: Throwable) {
    reactContext.reactApplicationContext.handleException(RuntimeException(t))
  }

  private val reactContext: ThemedReactContext
    get() = this.context as ThemedReactContext

  override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
    mJSTouchDispatcher.handleTouchEvent(event, mEventDispatcher!!)
    return super.onInterceptTouchEvent(event)
  }

  override fun onTouchEvent(event: MotionEvent): Boolean {
    mJSTouchDispatcher.handleTouchEvent(event, mEventDispatcher!!)
    super.onTouchEvent(event)
    // In case when there is no children interested in handling touch event, we return true from
    // the root view in order to receive subsequent events related to that gesture
    return true
  }

  override fun onChildStartedNativeGesture(ev: MotionEvent) {
    this.onChildStartedNativeGesture(null as View?, ev)
  }

  override fun onChildStartedNativeGesture(childView: View?, ev: MotionEvent) {
    mJSTouchDispatcher.onChildStartedNativeGesture(
      ev,
      mEventDispatcher!!
    )
  }

  override fun onChildEndedNativeGesture(childView: View, ev: MotionEvent) {
    mJSTouchDispatcher.onChildEndedNativeGesture(
      ev,
      mEventDispatcher!!
    )
  }

  override fun requestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
  }
}
