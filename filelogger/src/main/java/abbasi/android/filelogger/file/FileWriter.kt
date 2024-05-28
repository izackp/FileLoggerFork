/*
*
* Copyright (c) 2022 Abolfazl Abbasi
*
* */

package abbasi.android.filelogger.file

import abbasi.android.filelogger.time.FastDateFormat
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.util.Locale

internal class FileWriter(
    logFileManager: LogFileManager,
    dataFormatterPattern: String,
    startLogs: Map<String, String>?,
) {
    private var streamWriter: OutputStreamWriter? = null
    private var dateFormat: FastDateFormat? = null
    private var logFile: File? = null

    val openedFilePath
        get() = logFile?.absolutePath

    init {
        dateFormat = FastDateFormat.getInstance(dataFormatterPattern, Locale.US)
        try {
            val fileName = "${dateFormat?.format(System.currentTimeMillis())}.txt"
            logFile = logFileManager.createLogFile(fileName)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            logFile?.createNewFile()
            val stream = FileOutputStream(logFile)
            streamWriter = OutputStreamWriter(stream).apply {
                write("File logger started at ${dateFormat?.format(System.currentTimeMillis())}\n")
                startLogs?.forEach {
                    write("${it.key}: ${it.value}\n")
                }
                write("\n\n")
                flush()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun write(message: String) {
        streamWriter.takeIf { dateFormat != null }?.let { writer ->
            try {
                writer.write("${dateFormat?.format(System.currentTimeMillis())} $message")
                writer.flush()
            } catch (e: Exception) {
                Log.e(javaClass.simpleName, "e:", e)
            }
        }
    }
}