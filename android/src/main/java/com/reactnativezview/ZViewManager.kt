package com.reactnativezview

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp
import com.facebook.react.views.view.ReactViewGroup
import com.facebook.react.views.view.ReactViewManager

class ZViewManager(var mCallerContext: ReactApplicationContext) : ReactViewManager() {
  override fun getName(): String {
    return REACT_CLASS
  }

  @ReactProp(name = "pointerEvents")
  override fun setPointerEvents(view: ReactViewGroup, pointerEventsStr: String?) {
    super.setPointerEvents(view, pointerEventsStr)
  }

  @ReactProp(name = "touchable", defaultBoolean = true)
  fun setTouchable(view: ZView, touchable: Boolean) {
    view.setTouchable(touchable)
  }

  @ReactProp(name = "coordinates")
  fun setCoordinates(view: ZView, coords: ReadableMap?) {
    view.setCoords(coords)
  }

  override fun createViewInstance(context: ThemedReactContext): ZView {
    return ZView(context)
  }

  companion object {
    const val REACT_CLASS: String = "ZView"
  }
}
