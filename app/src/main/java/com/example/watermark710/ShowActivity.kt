package com.example.watermark710

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.watermark710.databinding.ActivityShowBinding
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


class ShowActivity: AppCompatActivity() {
    lateinit var binding: ActivityShowBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityShowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var uri:Uri = Uri.parse(intent.getStringExtra("sltImage"))

        binding.imgView.setImageURI(uri)

//        binding.svB.setOnClickListener{
//            var imgName:String = "temp111.PNG"
//            var bm:Bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,uri)
//            Log.v("1","1")
//            bitmapSave(bm,imgName)
//            Log.v("2","2")
//            Toast.makeText(this,"저장완",Toast.LENGTH_LONG).show()
//            Log.v("3","3")
//        }
        binding.waterMarkText.setVisibility(View.INVISIBLE)
        var visible:Boolean = false

        //워마 있고 없고
        binding.mkB.setOnClickListener {
            if(visible) {
                visible=false
                binding.waterMarkText.setVisibility(View.INVISIBLE)
            }else{
                visible=true
                binding.waterMarkText.setVisibility(View.VISIBLE)
            }
        }
//        val photoZone = binding.photoZone
//        var layer:View = photoZone
//        var newbm:Bitmap? = createBitmapFromView(this,layer)

        binding.svB.setOnClickListener{
            var imgName:String = "temp111.PNG"
            var bm:Bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,uri)

            val photoZone = binding.photoZone
            var layer:View = photoZone
            var newbm:Bitmap? = createBitmapFromView(this,layer)
            bm = newbm!!
            if(!checkPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) ||
                !checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "권한문제", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            //그림 저장
            if(!imageExternalSave(this, bm, this.getString(R.string.app_name))){
                Toast.makeText(this, "그림 저장을 실패하였습니다", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(this, "그림이 갤러리에 저장되었습니다", Toast.LENGTH_SHORT).show()
        }

    }
    private fun createBitmapFromView(ctx: Context, view: View): Bitmap {

        view.layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.MATCH_PARENT
        )
        val dm = ctx.resources.displayMetrics
        view.measure(
            View.MeasureSpec.makeMeasureSpec(dm.widthPixels, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(dm.heightPixels, View.MeasureSpec.EXACTLY)
        )
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        val bitmap = Bitmap.createBitmap(
            view.measuredWidth,
            view.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        view.layout(view.left, view.top, view.right, view.bottom)
        view.draw(canvas)
        return bitmap
    }
    fun imageExternalSave(context: Context, bitmap: Bitmap, path: String): Boolean {
        val state = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED == state) {

            val rootPath =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                    .toString()
            val dirName = "/" + path
            val fileName = System.currentTimeMillis().toString() + ".png"
            val savePath = File(rootPath + dirName)
            savePath.mkdirs()

            val file = File(savePath, fileName)
            if (file.exists()) file.delete()

            try {
                val out = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                out.flush()
                out.close()

                //갤러리 갱신
                context.sendBroadcast(
                    Intent(
                        Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.parse("file://" + Environment.getExternalStorageDirectory())
                    )
                )

                return true
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return false
    }
    fun checkPermission(activity: Activity, permission: String): Boolean {
        val permissionChecker =
            ContextCompat.checkSelfPermission(activity.applicationContext, permission)

        //권한이 없으면 권한 요청
        if (permissionChecker == PackageManager.PERMISSION_GRANTED) return true
        ActivityCompat.requestPermissions(activity, arrayOf(permission), 3000)
        return false
    }


    fun bitmapSave(bitmap: Bitmap, fileNameToSave: String): File? { // File name like "image.png"
        //create a file to write bitmap data
        var file: File? = null
        return try {
            file = File(Environment.getExternalStorageDirectory().toString() + File.separator + fileNameToSave)
            file.createNewFile()

            //Convert bitmap to byte array
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos) // YOU can also save it in JPEG
            val bitmapdata = bos.toByteArray()

            //write the bytes in file
            val fos = FileOutputStream(file)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()
            Log.v("s","success!")
            Toast.makeText(this, "저장완", Toast.LENGTH_SHORT).show()
            file
        } catch (e: Exception) {
            Log.v("E",e.toString())
            e.printStackTrace()
            Toast.makeText(this, "저장실", Toast.LENGTH_SHORT).show()
            file // it will return null
        }
    }

}