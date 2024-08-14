package com.reactnativechildprocess

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.Promise
import com.facebook.react.modules.core.DeviceEventManagerModule
import android.util.Log
import java.io.*
import java.util.concurrent.*


class ChildprocessModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    override fun getName(): String {
        return "Childprocess"
    }

    // See https://facebook.github.io/react-native/docs/native-modules-android
    @ReactMethod
    fun spawn(command: String, args: String[], promise: Promise) {
      val mutableList = args.toMutableList()
      mutableList.add(0, command)
      val params = mutableList.toTypedArray()
      val process = Runtime.getRuntime().exec(params)
      
      val output = process.getInputStream().bufferedReader().use(BufferedReader::readText)
      process.waitFor(10, TimeUnit.SECONDS)
      
      reactContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
            .emit("stdout", output)
    }

    
}
