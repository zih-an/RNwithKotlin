package com.rnwithkotlin

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.*
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.Promise;
import com.rnwithkotlin.data.Device
import com.rnwithkotlin.data.Person

import android.media.Image
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer


class InterfaceMovenet(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
    private final var TAG = "InterfaceMovenet";
    private lateinit var net: MoveNet;
    private var context:Context = reactContext;

    override fun getName(): String {
        return "InterfaceMovenet"
    }

    @ExperimentalGetImage
    private fun toBitmap(imageProxy: ImageProxy): Bitmap {
        val image: Image? = imageProxy.image
        val planes: Array<Image.Plane> = image!!.getPlanes()
        val yBuffer: ByteBuffer = planes[0].getBuffer()
        val uBuffer: ByteBuffer = planes[1].getBuffer()
        val vBuffer: ByteBuffer = planes[2].getBuffer()
        val ySize: Int = yBuffer.remaining()
        val uSize: Int = uBuffer.remaining()
        val vSize: Int = vBuffer.remaining()
        val nv21 = ByteArray(ySize + uSize + vSize)
        //U and V are swapped
        yBuffer.get(nv21, 0, ySize)
        vBuffer.get(nv21, ySize, vSize)
        uBuffer.get(nv21, ySize + vSize, uSize)
        val yuvImage = YuvImage(nv21, ImageFormat.NV21, image.getWidth(), image.getHeight(), null)
        val out = ByteArrayOutputStream()
        yuvImage.compressToJpeg(Rect(0, 0, yuvImage.width, yuvImage.height), 75, out)
        val imageBytes: ByteArray = out.toByteArray()
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    @ReactMethod
    fun create(promise: Promise) {
        net = MoveNet.create(context, Device.CPU, ModelType.Thunder);
        promise.resolve("in movenet create")
    }

    @SuppressLint("UnsafeOptInUsageError")
    @ReactMethod
    fun estimatePoses(frame: ImageProxy, promise: Promise): List<Person>? {
        if (net == null) {
            promise.reject("Net is NULL", "a");
            return null;
        }
        val bitmap: Bitmap = toBitmap(frame)
        return net.estimatePoses(bitmap)}

    @ReactMethod
    fun shutdown(promise: Promise) {
        if (net == null) {
            promise.reject("Net is NULL", "a");
        }
        net.close()
    }
}