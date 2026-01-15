package com.leecoder.stockchart.util.log

import android.util.Log
import com.leecoder.stockchart.util.BuildConfig

object Logger {

    private const val DEFAULT_TAG = "[Bolly]"

    private val isLoggable: Boolean
        get() = BuildConfig.DEBUG

    fun d(message: String, tag: String = DEFAULT_TAG) {
        if (isLoggable) {
            Log.d(tag, message)
        }
    }

    fun d(tag: String = DEFAULT_TAG, message: () -> String) {
        if (isLoggable) {
            Log.d(tag, message())
        }
    }

    fun i(message: String, tag: String = DEFAULT_TAG) {
        if (isLoggable) {
            Log.i(tag, message)
        }
    }

    fun i(tag: String = DEFAULT_TAG, message: () -> String) {
        if (isLoggable) {
            Log.i(tag, message())
        }
    }

    fun w(message: String, tag: String = DEFAULT_TAG, throwable: Throwable? = null) {
        if (isLoggable) {
            if (throwable != null) {
                Log.w(tag, message, throwable)
            } else {
                Log.w(tag, message)
            }
        }
    }

    fun w(tag: String = DEFAULT_TAG, throwable: Throwable? = null, message: () -> String) {
        if (isLoggable) {
            if (throwable != null) {
                Log.w(tag, message(), throwable)
            } else {
                Log.w(tag, message())
            }
        }
    }

    fun e(message: String, tag: String = DEFAULT_TAG, throwable: Throwable? = null) {
        if (isLoggable) {
            if (throwable != null) {
                Log.e(tag, message, throwable)
            } else {
                Log.e(tag, message)
            }
        }
    }

    fun e(tag: String = DEFAULT_TAG, throwable: Throwable? = null, message: () -> String) {
        if (isLoggable) {
            if (throwable != null) {
                Log.e(tag, message(), throwable)
            } else {
                Log.e(tag, message())
            }
        }
    }
}
