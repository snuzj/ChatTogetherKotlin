package com.snuzj.chattogetherkotlin.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.snuzj.chattogetherkotlin.databinding.ActivityNumberBinding

class NumberActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNumberBinding

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNumberBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //init firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        //handle click, submit button
        binding.submitBtn.setOnClickListener {
            validateData()
        }



    }

    private fun validateData() {
        //check number is entered or not
        if(binding.numberEt.text!!.isEmpty()){
            Toast.makeText(this,"Hãy nhập số điện thoại của bạn", Toast.LENGTH_SHORT).show()
        }
        else{
            val intent = Intent(this,OTPActivity::class.java)
            intent.putExtra("number",binding.numberEt.text!!.toString())
            startActivity(intent)
        }

    }

    private fun checkUser() {
        if (firebaseAuth.currentUser != null){
            startActivity(Intent(this,MainActivity::class.java))
        }
    }
}