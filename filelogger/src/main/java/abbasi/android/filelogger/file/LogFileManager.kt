package abbasi.android.filelogger.file

import abbasi.android.filelogger.config.Constance.Companion.DIRECTORY
import abbasi.android.filelogger.time.FastDateFormat
import android.content.Context
import java.io.File

internal class LogFileManager(
    context: Context,
    rootDir: String,
    private var dateFormat: FastDateFormat,
) {

    private val prefs = context.getSharedPreferences("log_metadata", Context.MODE_PRIVATE)
    private val logsDirectory: String = rootDir + DIRECTORY
    private var currentFile: File? = null

    var lastCreationTime: Long = 0

    val logFilesList
        get() = File(logsDirectory).listFiles()?.filter { it.isFile }

    init {
        val logDir = File(logsDirectory)
        if (logDir.exists().not()) {
            logDir.mkdirs()
        }
    }

    fun currentLogFile(): File {
        val now = System.currentTimeMillis()
        val fileName = "${dateFormat.format(now)}.txt"

        val logFile = File(logsDirectory, fileName)
        logFile.createNewFile()
        prefs.edit().putLong(fileName, now).apply()
        lastCreationTime = now
        currentFile = logFile

        return logFile
    }

    fun getCreationTime(fileName: String): Long {
        return prefs.getLong(fileName, 0L)
    }

    fun deleteLogFile(fileName: String) {
        try {
            val file = File(logsDirectory, fileName)
            if (file.exists()) {
                file.delete()
                prefs.edit().remove(fileName).apply()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun deleteLogsDir() {
        File(logsDirectory).listFiles()?.filter {
            it.absolutePath != currentFile?.absolutePath
        }?.forEach {
            it.delete()
        }
    }
}