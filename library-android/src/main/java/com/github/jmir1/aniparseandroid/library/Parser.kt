package com.github.jmir1.aniparseandroid.library

import android.content.Context
import android.os.Build
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString

class Parser {
    companion object {
        /**
         * Json serialization object
         */
        private val json = Json { ignoreUnknownKeys = true }

        /**
         * Whether the python interpreter is started or not
         */
        private var isStarted: Boolean = false

        /**
         * Starts the python interpreter
         *
         * @return error code
         */
        fun start(context: Context): Int {
            val assetExtractor = AssetExtractor(context.applicationContext)
            val abi = Build.SUPPORTED_ABIS.first()
            val path = "python/$abi"
            assetExtractor.removeAssets(path)
            assetExtractor.copyAssets(path)
            val pythonPath = assetExtractor.getAssetsDataDir() + path
            val returnCode = startExternal(pythonPath)
            isStarted = returnCode == 0
            return returnCode
        }

        private external fun startExternal(path: String): Int

        /**
         * Stops the python interpreter
         *
         * @return error code
         */
        fun stop(): Int {
            val returnCode = stopExternal()
            isStarted = returnCode == 0
            return returnCode
        }

        private external fun stopExternal(): Int

        /**
         * Sends the [payload] to the interpreter and returns the result.
         *
         * @param payload
         */
        private external fun call(payload: String): String?

        /**
         * Parse an anime filename
         *
         * @param input The filename to be parsed
         * @return The parsed result or null if there was an error
         * @throws [InterpreterException] if the interpreter isn't started
         */
        fun parse(input: String): AniparseResult? {
            if (!isStarted) throw InterpreterException()
            val jsonString = call("json.dumps(aniparse.parse('$input'))")
                ?: return null
            return json.decodeFromString<AniparseResult>(jsonString)
        }

        /**
         * Load the library
         */
        init {
            System.loadLibrary("aniparse")
        }
    }
}
