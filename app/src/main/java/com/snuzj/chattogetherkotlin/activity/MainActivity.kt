package com.snuzj.chattogetherkotlin.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.snuzj.chattogetherkotlin.adapter.ViewPagerAdapter
import com.snuzj.chattogetherkotlin.databinding.ActivityMainBinding
import com.snuzj.chattogetherkotlin.ui.CallFragment
import com.snuzj.chattogetherkotlin.ui.ChatFragment
import com.snuzj.chattogetherkotlin.ui.StatusFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //init firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        //init viewPager
        val fragmentArrayList = ArrayList<Fragment>()

        fragmentArrayList.add(ChatFragment())
        fragmentArrayList.add(StatusFragment())
        fragmentArrayList.add(CallFragment())

        val adapter = ViewPagerAdapter(this, supportFragmentManager, fragmentArrayList)

        binding.viewPager.adapter = adapter

        //handle click, tab layout
        binding.tabs.setupWithViewPager(binding.viewPager)



    }

    private fun checkUser() {
        if(firebaseAuth.currentUser == null){
            startActivity(Intent(this,NumberActivity::class.java))
            finish()
        }
    }

}