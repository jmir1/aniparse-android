package com.github.jmir1.aniparseandroid.library.android

import android.content.Context
import android.content.res.AssetManager
import android.preference.PreferenceManager
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.Locale


class AssetExtractor(context: Context) {
    private val mContext: Context = context
    private val mAssetManager: AssetManager = context.assets

    /**
     * Sets a version for the extracted assets version.
     *
     * @param version: int
     */
    fun setAssetsVersion(version: Int) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(mContext)
        val editor = preferences.edit()
        editor.putInt("assetsVersion", version)
        editor.apply()
    }

    /**
     * Returns the version for the extracted assets.
     *
     * @return int
     */
    fun getAssetsVersion(): Int {
        val preferences = PreferenceManager.getDefaultSharedPreferences(mContext)
        return preferences.getInt("assetsVersion", 0)
    }

    /**
     * Returns a list of assets in the APK.
     *
     * @param path: the path in the assets folder.
     * @return the list of assets.
     */
    fun listAssets(path: String): List<String> {
        val assets: MutableList<String> = ArrayList()
        try {
            val assetList = mAssetManager.list(path)
            if (assetList!!.isNotEmpty()) {
                for (asset in assetList) {
                    val subAssets = listAssets("$path/$asset")
                    assets.addAll(subAssets)
                }
            } else {
                assets.add(path)
            }
        } catch (e: IOException) {
            Log.e(LOGTAG, e.stackTraceToString())
        }
        return assets
    }

    /**
     * Returns the path to the assets data dir on the device.
     *
     * @return String with the data dir path.
     */
    fun getAssetsDataDir(): String {
        val appDataDir: String = mContext.applicationInfo.dataDir
        return "$appDataDir/assets/"
    }

    /**
     * Copies an asset from the APK to the device.
     *
     * @param src: the source path in the APK.
     * @param dst: the destination path in the device.
     */
    private fun copyAssetFile(src: String, dst: String) {
        val file = File(dst)
        Log.i(LOGTAG, String.format(Locale.ENGLISH, "Copying %s -> %s", src, dst))
        try {
            val dir = file.parentFile!!
            if (!dir.exists()) {
                dir.mkdirs()
            }
            val inputStream: InputStream = mAssetManager.open(src)
            val outputStream: OutputStream = FileOutputStream(file)
            val buffer = ByteArray(BUFSIZE)
            var read: Int = inputStream.read(buffer)
            while (read != -1) {
                outputStream.write(buffer, 0, read)
                read = inputStream.read(buffer)
            }
            outputStream.close()
            inputStream.close()
        } catch (e: IOException) {
            Log.e(LOGTAG, e.stackTraceToString())
        }
    }

    /**
     * Copies the assets from the APK to the device.
     *
     * @param path: the source path
     */
    fun copyAssets(path: String) {
        for (asset in listAssets(path)) {
            copyAssetFile(asset, getAssetsDataDir() + asset)
        }
    }

    /**
     * Recursively deletes the contents of a folder.
     *
     * @param file: the File object.
     */
    private fun recursiveDelete(file: File) {
        if (file.isDirectory) {
            for (f in file.listFiles()!!) recursiveDelete(f)
        }
        Log.i(LOGTAG, "Removing " + file.absolutePath)
        file.delete()
    }

    /**
     * Removes recursively the assets from the device.
     *
     * @param path: the path to the assets folder
     */
    fun removeAssets(path: String) {
        val file = File(getAssetsDataDir() + path)
        recursiveDelete(file)
    }

    /**
     * Returns if the path exists in the device assets.
     *
     * @param path: the path to the assets folder
     * @return Boolean
     */
    fun existsAssets(path: String): Boolean {
        val file = File(getAssetsDataDir() + path)
        return file.exists()
    }
}

private const val BUFSIZE = 1024
private const val LOGTAG = "AssetExtractor"
