package com.example.watermark710

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SplashActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)
        // Initialize Firebase Auth
        auth = Firebase.auth
        try{
            Log.e("Splash id",auth.currentUser!!.uid)
            Toast.makeText(this, "로그인되어있는기기",
                Toast.LENGTH_SHORT).show()
            Handler().postDelayed({
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            },3000)

        }catch(e:Exception){
            Log.e("Splash msg","회원가입 해줘야함")
            auth.signInAnonymously()
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInAnonymously:비회원 로그인 성공")
                        Toast.makeText(this, "비회원 로그인 성공",
                            Toast.LENGTH_SHORT).show()
                        val user = auth.currentUser
//                        updateUI(user)
                        Handler().postDelayed({
                            startActivity(Intent(this,MainActivity::class.java))
                            finish()
                        },3000)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInAnonymously:failure", task.exception)
                        Toast.makeText(this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
//                        updateUI(null)
                    }
                }
        }


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        
    }
}