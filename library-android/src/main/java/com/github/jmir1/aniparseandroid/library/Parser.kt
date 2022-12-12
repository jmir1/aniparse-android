package com.github.jmir1.aniparseandroid.library

import android.content.Context
import android.os.Build

class Parser {
    companion object {
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
            val pythonPath = context.applicationInfo.nativeLibraryDir
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
        fun parse(input: String): String? {
            if (!isStarted) throw InterpreterException()
            return call("aniparse.parse('$input')")
        }

        /**
         * Load the library
         */
        init {
            System.loadLibrary("aniparse")
        }
    }
}
