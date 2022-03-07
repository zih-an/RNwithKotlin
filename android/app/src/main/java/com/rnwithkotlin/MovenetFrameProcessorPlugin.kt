package com.rnwithkotlin

import Jama.Matrix
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.*
import android.media.Image
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.WritableNativeArray
import com.mrousavy.camera.frameprocessor.FrameProcessorPlugin
import com.rnwithkotlin.data.Device
import com.rnwithkotlin.utils.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.lang.Exception
import java.nio.ByteBuffer
import java.util.*


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
    private fun estimatePoses(frame: ImageProxy): WritableNativeArray {
        val screenHeight = frame.height
        val screenWidth = frame.width
        Log.d("size: ", "w: ${screenWidth} h: ${screenHeight}")

        if (net == null) {
            return WritableNativeArray();
        }
        val bitmap: Bitmap = toBitmap(frame)
        val ans = net.estimatePoses(bitmap)
        val matrixList = DataTypeTransfor().listPerson2ListMatrix_Jama(ans)
//        val zoomans = frameZoom(matrixList, frame, bitmap)
//        saveImage(bitmap)

        return ListMatrix2WritableArray(matrixList)
    }

    //this function will save |
    private fun saveImage(bitmap:Bitmap) {
        var fos: OutputStream;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val resolver:ContentResolver = context.getContentResolver();
                val contentValues:ContentValues =  ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "Image" + ".jpg");
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg");
                val imageUri: Uri? = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                fos = Objects.requireNonNull(imageUri)?.let { resolver.openOutputStream(it) }!!;
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos); Objects.requireNonNull(fos);
            }
        } catch (e:Exception) {
            Log.d("error",e.toString());
        }
    }

    private fun frameZoom(obj:List<Matrix>, frame: ImageProxy, bitmap: Bitmap):List<Matrix> {
        val screenHeight = frame.height
        val screenWidth = frame.width
        val imageHeight = bitmap.height
        val imageWidth = bitmap.width

        val HeightRatio:Double=screenHeight.toDouble()/imageHeight.toDouble()
        val WidthRatio:Double=screenWidth.toDouble()/imageWidth.toDouble()
        var afterZoom_obj:MutableList<Matrix> = arrayListOf<Matrix>()
        val sampleNun=afterZoom_obj.count()

        obj.forEach {
            var temp:Matrix= Matrix(it.rowDimension,it.columnDimension)
            for(i in 0..it.rowDimension-1)
            {
                for(j in 0..it.columnDimension-1)
                {
                    when(j)
                    {
                        0->{
                            temp.set(i,j,temp.get(i,j)*WidthRatio)
                        }
                        1->{
                            temp.set(i,j,temp.get(i,j)*HeightRatio)
                        }
                    }
                }
            }
            afterZoom_obj.add(temp)
        }
        return afterZoom_obj
    }

    private fun ListMatrix2WritableArray(obj:List<Matrix>):WritableNativeArray {
        var obj2:WritableNativeArray = WritableNativeArray()
        obj.forEach{
            var temp:WritableNativeArray = WritableNativeArray()
            val rowNum=it.rowDimension
            val colNum=it.columnDimension
            for(i in 0..rowNum-1)
            {
                var ttemp:WritableNativeArray = WritableNativeArray()
                for(j in 0..colNum-1)
                {
                    ttemp.pushDouble(it.get(i,j))
                }
                temp.pushArray(ttemp)
            }
            obj2.pushArray(temp)
        }
        return obj2
    }

//    fun shutdown() {
//        if (net == null) {
//            return;
//        }
//        net.close()
//    }
}

