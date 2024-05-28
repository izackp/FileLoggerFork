/*
*
* Copyright (c) 2022 Abolfazl Abbasi
*
* */

package abbasi.android.filelogger

import abbasi.android.filelogger.config.Config
import abbasi.android.filelogger.file.FileWriter
import abbasi.android.filelogger.file.LogFileManager
import abbasi.android.filelogger.file.LogLevel
import abbasi.android.filelogger.file.RetentionPolicyChecker
import abbasi.android.filelogger.threading.ThreadQueue
import abbasi.android.filelogger.util.FileZipper
import android.content.Context
import android.util.Log
import java.io.File

object FileLogger {

    private var initialized = false
    var isEnable: Boolean = true

    private var config: Config? = null
    private lateinit var retentionChecker: RetentionPolicyChecker
    private lateinit var logFileManager: LogFileManager
    private lateinit var fileWriter: FileWriter

    private val fileZipper: FileZipper by lazy {
        FileZipper()
    }

    private var logQueue: ThreadQueue = ThreadQueue("LogQueue")

    fun init(context: Context, config: Config) {
        if (initialized) {
            return
        }

        this.config = config

        logFileManager = LogFileManager(
            context = context.applicationContext,
            rootDir = config.directory
        )

        retentionChecker = RetentionPolicyChecker(logFileManager)

        fileWriter = FileWriter(
            logFileManager = logFileManager,
            dataFormatterPattern = config.dataFormatterPattern,
            startLogs = config.startupData
        )

        initialized = true

        config.retentionPolicy?.let {
            retentionChecker(policy = it)
        }
    }

    fun i(tag: String? = config?.defaultTag, msg: String) = checkBlock {
        if (config?.logcatEnable == true) {
            Log.i(tag, msg)
        }

        postLog(LogLevel.Info, tag, msg)
    }

    fun e(
        tag: String? = config?.defaultTag,
        msg: String,
        throwable: Throwable? = null
    ) = checkBlock {
        if (config?.logcatEnable == true) {
            Log.e(tag, msg, throwable)
        }

        postLog(logLevel = LogLevel.Error, tag = tag, msg = msg, throwable = throwable)
    }

    fun e(
        tag: String? = config?.defaultTag,
        throwable: Throwable
    ) = checkBlock {
        if (config?.logcatEnable == true) {
            Log.e(tag, "", throwable)
        }

        postLog(logLevel = LogLevel.Error, tag = tag, throwable = throwable)
    }

    fun w(
        tag: String? = config?.defaultTag,
        msg: String
    ) = checkBlock {
        if (config?.logcatEnable == true) {
            Log.w(tag, msg)
        }

        postLog(logLevel = LogLevel.Warning, tag = tag, msg = msg)
    }

    fun d(
        tag: String? = config?.defaultTag,
        msg: String
    ) = checkBlock {
        if (config?.logcatEnable == true) {
            Log.d(tag, msg)
        }

        postLog(logLevel = LogLevel.Debug, tag = tag, msg = msg)
    }

    private fun postLog(
        logLevel: LogLevel,
        tag: String? = config?.defaultTag,
        msg: String? = "",
        throwable: Throwable? = null
    ) = fileWriter.let { writer ->
        logQueue.postRunnable {
            val message = config?.logInterceptor?.intercept(
                logLevel, tag.orEmpty(), msg.orEmpty(), throwable
            ) ?: msg

            val stringBuilder = StringBuilder("$logLevel/$tag: $message\n")

            throwable?.let { exception ->
                exception.stackTrace.forEach { element ->
                    stringBuilder.append("\t $element\n")
                }
            }

            writer.write(stringBuilder.toString())
        }
    }

    fun deleteFiles() = checkBlock {
        i(msg = "FileLogger delete files called")
        logFileManager.deleteLogsDir(fileWriter.openedFilePath)
    }

    fun compressLogsInZipFile(
        zipFileName: String? = null,
        callback: ((zipFile: File?) -> Unit),
    ) = checkBlock {
        config?.let {
            fileZipper.compressFiles(it, zipFileName, callback)
        }
    }

    private fun checkBlock(block: () -> Unit) {
        if (initialized && isEnable) {
            block()
        } else if (isEnable) {
            Log.e(
                javaClass.simpleName,
                "SDK not initialized maybe forgot call FileLogger.init(config: Config)"
            )
        }
    }
}