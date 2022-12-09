package com.github.jmir1.aniparseandroid.app

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.jmir1.aniparseandroid.app.databinding.ActivityMainBinding
import com.github.jmir1.aniparseandroid.library.android.AssetExtractor
import com.github.jmir1.aniparseandroid.library.android.NotificationUtil
import com.github.jmir1.aniparseandroid.library.android.Parser
import java.lang.IllegalStateException

class MainActivity : AppCompatActivity() {

    private val notificationUtil: NotificationUtil by lazy { NotificationUtil(this) }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonCompute.setOnClickListener {
            val assetExtractor = AssetExtractor(applicationContext)
            assetExtractor.removeAssets("python")
            assetExtractor.copyAssets("python")
            val pythonPath = assetExtractor.getAssetsDataDir() + "python"
            val greeting = Parser.greetDumb()
            Parser.start(pythonPath)
            Log.i("bruh", greeting)
            if (binding.editTextFactorial.text.isNotEmpty()) {
                val input = binding.editTextFactorial.text.toString().toLong()
                val result = try {
                    input.toString()
                } catch (ex: IllegalStateException) {
                    "Error: ${ex.message}"
                }

                binding.textResult.text = result
                binding.textResult.visibility = View.VISIBLE
                notificationUtil.showNotification(
                    context = this,
                    title = getString(R.string.notification_title),
                    message = result
                )
            } else {
                Toast.makeText(this, "Please enter a number", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
