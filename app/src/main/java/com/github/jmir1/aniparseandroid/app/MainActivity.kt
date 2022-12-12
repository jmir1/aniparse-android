package com.github.jmir1.aniparseandroid.app

import android.os.Build
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
        val assetExtractor = AssetExtractor(applicationContext)
        val abi = Build.SUPPORTED_ABIS.first()
        val path = "python/$abi"
        assetExtractor.removeAssets(path)
        assetExtractor.copyAssets(path)
        val pythonPath = assetExtractor.getAssetsDataDir() + path
        Parser.start(pythonPath)

        binding.buttonCompute.setOnClickListener {
            Parser.parse("[TaigaSubs]_Toradora!_(2008)_-_01v2_-_Tiger_and_Dragon_[1280x720_H.264_FLAC][1234ABCD].mkv")
            val input = binding.editTextFactorial.text.toString()
            val result = Parser.parse(input)

            binding.textResult.text = result
            binding.textResult.visibility = View.VISIBLE
            notificationUtil.showNotification(
                context = this,
                title = getString(R.string.notification_title),
                message = result
            )
        }
    }

    override fun onDestroy() {
        Parser.stop()
        super.onDestroy()
    }
}
