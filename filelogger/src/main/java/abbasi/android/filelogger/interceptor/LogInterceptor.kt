package abbasi.android.filelogger.interceptor

import abbasi.android.filelogger.file.LogLevel

interface LogInterceptor {
    fun intercept(level: LogLevel, tag: String, message: String, e: Throwable?): String
}