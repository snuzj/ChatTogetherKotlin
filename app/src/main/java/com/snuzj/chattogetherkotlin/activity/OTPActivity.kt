@file:Suppress("DEPRECATION")

package com.snuzj.chattogetherkotlin.activity

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.snuzj.chattogetherkotlin.databinding.ActivityOtpBinding
import java.util.concurrent.TimeUnit

class OTPActivity : AppCompatActivity() {

    private lateinit var  binding: ActivityOtpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var verificationId: String
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //init firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()

        //init progressDialog
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Đang tải...")
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()

        //mobile authentication
        numberAuth()

        //handle click, verify otp
        binding.verifyBtn.setOnClickListener {
            validateData()
        }


    }

    private fun validateData() {
        //get data
        if(binding.otpEt.text!!.isEmpty()){
            Toast.makeText(this,"Hãy nhập mã xác minh",Toast.LENGTH_SHORT).show()
        }
        else{
            progressDialog.show()
            val credential = PhoneAuthProvider.getCredential(verificationId, binding.otpEt.text!!.toString())

            firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener{
                    if(it.isSuccessful){
                        progressDialog.dismiss()
                        startActivity(Intent(this,ProfileActivity::class.java))
                        finish()
                    }
                    else{
                        Toast.makeText(this,"Lỗi ${it.exception}",Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }


    private fun numberAuth() {
        val phoneNumber = "+84" + intent.getStringExtra("number")

        Log.d("NUMBER", "numberAuth: "+phoneNumber)
        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {

                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    progressDialog.dismiss()
                    Toast.makeText(this@OTPActivity,"Mã OTP không chính xác",Toast.LENGTH_SHORT).show()
                }

                override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(p0, p1)

                    progressDialog.dismiss()
                    verificationId = p0
                }

            }).build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }
}