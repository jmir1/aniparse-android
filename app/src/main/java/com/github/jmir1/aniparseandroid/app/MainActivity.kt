package com.github.jmir1.aniparseandroid.app

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.github.jmir1.aniparseandroid.app.databinding.ActivityMainBinding
import com.github.jmir1.aniparseandroid.library.Parser

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Parser.start(this)

        binding.buttonCompute.setOnClickListener {
            Parser.parse("[TaigaSubs]_Toradora!_(2008)_-_01v2_-_Tiger_and_Dragon_[1280x720_H.264_FLAC][1234ABCD].mkv")
            val input = binding.editTextFactorial.text.toString()
            val result = Parser.parse(input)

            binding.textResult.text = result
            binding.textResult.visibility = View.VISIBLE
        }
    }

    override fun onDestroy() {
        Parser.stop()
        super.onDestroy()
    }
}
