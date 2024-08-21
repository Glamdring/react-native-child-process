package com.reactnativechildprocess

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.Promise
import com.facebook.react.modules.core.DeviceEventManagerModule
import android.util.Log
import java.io.*
import kotlin.collections.*
import java.util.concurrent.*


class ChildprocessModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    override fun getName(): String {
        return "Childprocess"
    }

    // See https://facebook.github.io/react-native/docs/native-modules-android
    @ReactMethod
    fun spawn(command: String, args: ReadableArray, opts: ReadableMap): String? {
      try {
        val mutableList = arrayListOf<String>()
        mutableList.add(command)
        for (i in 0..args.size() - 1) {
          mutableList.add(args.getString(i))
        }
        val params = mutableList.toTypedArray()
        val process = Runtime.getRuntime().exec(params)

        val output = process.getInputStream().bufferedReader().use(BufferedReader::readText)
        process.waitFor(5, TimeUnit.SECONDS)

        Log.i("ChildprocessModule", output)

        if (false) { // TODO get 'synchrounous' from options
          // TODO implement asynchronous
          getReactApplicationContext()
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
            .emit("onOutput", output)

          return ""
        } else {
          return output
        }
      } catch (ex: Exception) {
        Log.e("ChildprocessModule", ex.message, ex)
        return ex.message
      }
    }


}
