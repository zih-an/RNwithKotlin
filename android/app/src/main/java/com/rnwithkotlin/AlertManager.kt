package com.rnwithkotlin

import android.content.Intent
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.Promise;

class AlertManager(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
    private final var TAG = "AlertMangager";
    override fun getName(): String {
        return "Alt"
    }

    @ReactMethod
    // have to use promise to get the result async.
    fun trigger(a:Double, b:Double, resPromise:Promise) {
        resPromise.resolve(a+b);
    }

}