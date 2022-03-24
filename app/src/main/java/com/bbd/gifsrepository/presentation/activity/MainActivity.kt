package com.bbd.gifsrepository.presentation.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.bbd.gifsrepository.R
import com.bbd.gifsrepository.databinding.ActivityMainBinding
import com.bbd.gifsrepository.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}