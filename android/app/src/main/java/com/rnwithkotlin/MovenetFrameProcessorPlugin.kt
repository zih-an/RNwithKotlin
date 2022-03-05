package com.rnwithkotlin
import android.util.Log
import androidx.camera.core.ImageProxy
import com.mrousavy.camera.frameprocessor.FrameProcessorPlugin
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import com.facebook.react.bridge.ReactApplicationContext
import com.rnwithkotlin.data.Device
import com.rnwithkotlin.utils.*
import android.media.Image
import androidx.camera.core.ExperimentalGetImage
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer


class MovenetFrameProcessorPlugin(reactContext: ReactApplicationContext): FrameProcessorPlugin("MoveNet") {
    private lateinit var net: MoveNet;
    private var context:Context = reactContext;
    init {
        Log.d("TAG", "in create")
        net = MoveNet.create(context, Device.CPU, ModelType.Thunder);
        Log.d("TAG", "create finish")
    }

    override fun callback(image: ImageProxy, params: Array<Any>): Any? {
        // code goes here
        return estimatePoses(image);
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


    @SuppressLint("UnsafeOptInUsageError")
    private fun estimatePoses(frame: ImageProxy): Double {
        if (net == null) {
            return -1.0;
        }
        val bitmap: Bitmap = toBitmap(frame)
        val ans = net.estimatePoses(bitmap)
        val tmp: Int = ans.count()
        val matrixList = DataTypeTransfor().listPerson2ListMatrix_Jama(ans)
        for (i in 0..tmp - 1) {
            AffineTransprocess().printJama(matrixList.get(i))
        }
        return matrixList.get(0).get(0, 0);
    }

//    fun shutdown() {
//        if (net == null) {
//            return;
//        }
//        net.close()
//    }
}

