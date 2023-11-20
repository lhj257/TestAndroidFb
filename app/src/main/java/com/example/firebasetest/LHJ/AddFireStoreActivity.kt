package com.example.firebasetest.LHJ

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasetest.LHJ.databinding.ActivityAddFireStoreBinding

class AddFireStoreActivity : AppCompatActivity() {
    lateinit var binding : ActivityAddFireStoreBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFireStoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //FireStore CRUD
    }
}