package com.example.watermark710

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.watermark710.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding


    val DEFAULT_GALLERY_REQUEST_CODE = 0
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        //갤러리 열기
        binding.opengallery.setOnClickListener{
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, DEFAULT_GALLERY_REQUEST_CODE)
        }

        binding.openDiary.setOnClickListener {
            startActivity(Intent(this,DiaryActivity::class.java))

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            return
        }

        when (requestCode) {
            DEFAULT_GALLERY_REQUEST_CODE -> {
                data?:return
                val uri = data.data as Uri
                // 이미지 URI를 가지고 하고 싶은거 하면 된다.
                intent = Intent(this,ShowActivity::class.java)
                intent.putExtra("sltImage",uri.toString())
                startActivity(intent)
                Toast.makeText(this, uri.toString(), Toast.LENGTH_SHORT).show()
            }

            else -> {
                Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}