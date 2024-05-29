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

internal class FileWriter(
    private var dateFormat: FastDateFormat,
    logFile: File,
    startLogs: Map<String, String>?,
) {
    private var streamWriter: OutputStreamWriter? = null

    init {
        try {
            val stream = FileOutputStream(logFile)
            streamWriter = OutputStreamWriter(stream).apply {
                write("File logger started at ${dateFormat.format(System.currentTimeMillis())}\n")
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
        streamWriter?.let { writer ->
            try {
                writer.write("${dateFormat.format(System.currentTimeMillis())} $message")
                writer.flush()
            } catch (e: Exception) {
                Log.e(javaClass.simpleName, "e:", e)
            }
        }
    }

    fun close() {
        try {
            streamWriter?.close()
            streamWriter = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}