package com.rnwithkotlin

import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ViewManager
import com.mrousavy.camera.frameprocessor.FrameProcessorPlugin


class ReactModuleManager : ReactPackage {
    override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
        FrameProcessorPlugin.register(MovenetFrameProcessorPlugin(reactContext))
        return emptyList()
    }

    override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
        return emptyList()
    }
}

//import android.view.View
//import com.facebook.react.ReactPackage
//import com.facebook.react.bridge.NativeModule
//import com.facebook.react.bridge.ReactApplicationContext
//import com.facebook.react.uimanager.ViewManager
//import java.util.*
//
//class ReactModuleManager:ReactPackage {
//    override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
//        val modules = ArrayList<NativeModule>();
////        modules.add(AlertManager(reactContext));
////        modules.add(PoseDetection(reactContext));
//        modules.add(InterfaceMovenet(reactContext));
//        return modules;
//    }
//
//    override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
//        return Collections.emptyList<ViewManager<*, *>>()
//    }
//}
