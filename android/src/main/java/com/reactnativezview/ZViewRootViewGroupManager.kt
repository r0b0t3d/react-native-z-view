package com.reactnativezview

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.UIManagerHelper
import com.facebook.react.uimanager.annotations.ReactProp
import com.facebook.react.views.view.ReactViewGroup
import com.facebook.react.views.view.ReactViewManager


class ZViewRootViewGroupManager(var mCallerContext: ReactApplicationContext) : ReactViewManager() {
  override fun getName(): String {
    return REACT_CLASS
  }

  @ReactProp(name = "pointerEvents")
  override fun setPointerEvents(view: ReactViewGroup, pointerEventsStr: String?) {
    super.setPointerEvents(view, pointerEventsStr)
  }

  override fun addEventEmitters(reactContext: ThemedReactContext, view: ReactViewGroup) {
    super.addEventEmitters(reactContext, view)
    val dispatcher =
      UIManagerHelper.getEventDispatcherForReactTag(mCallerContext, view.id)
    (view as ZViewRootViewGroup).setEventDispatcher(dispatcher)
  }

  override fun createViewInstance(context: ThemedReactContext): ZViewRootViewGroup {
    return ZViewRootViewGroup(context)
  }

  companion object {
    const val REACT_CLASS: String = "ZViewRootViewGroup"
  }
}
