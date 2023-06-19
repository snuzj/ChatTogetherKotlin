@file:Suppress("DEPRECATION")

package com.snuzj.chattogetherkotlin.activity

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.snuzj.chattogetherkotlin.R
import com.snuzj.chattogetherkotlin.databinding.ActivityProfileBinding
import com.snuzj.chattogetherkotlin.model.UserModel
import java.util.Date

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var selectedImg: Uri
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //init firebase
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()

        //init progressDialog
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Đang tải...")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.userImage.setOnClickListener {
            selectedImage()
        }

        binding.submitBtn.setOnClickListener {
            validateData()
        }


    }

    private fun validateData() {
        val name = binding.nameEt.text.toString()
        //get data
        if(name.isEmpty()){
            Toast.makeText(this,"Hãy nhập tên của bạn",Toast.LENGTH_SHORT).show()
        }
        else{
            uploadData()
        }
    }

    private fun uploadData() {
        val ref = firebaseStorage.reference.child("Profile").child(Date().time.toString())
        ref.putFile(selectedImg).addOnCompleteListener{
            if(it.isSuccessful){
                ref.downloadUrl.addOnSuccessListener {task->
                    uploadInfo(task.toString())
                }
            }
        }
    }

    private fun uploadInfo(imgUrl: String) {
        val user = UserModel(firebaseAuth.uid.toString(), binding.nameEt.text.toString(), firebaseAuth.currentUser!!.phoneNumber.toString(), imgUrl)

        firebaseDatabase.reference.child("users")
            .child(firebaseAuth.uid.toString())
            .setValue(user)
            .addOnSuccessListener {
                Toast.makeText(this,"Thêm thành công", Toast.LENGTH_SHORT)
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }
    }

    private fun selectedImage() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(data != null){
            if (data.data != null){
                selectedImg = data.data!!
                binding.userImage.setImageURI(selectedImg)
            }
        }
    }
}