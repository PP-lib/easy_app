package com.example.easyapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.easyapp.databinding.ActivityLegacyBinding

class LegacyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLegacyBinding
    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLegacyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonIncrement.setOnClickListener {
            count++
            binding.textMessage.text = "Legacy Count: $count"
        }
    }
}
