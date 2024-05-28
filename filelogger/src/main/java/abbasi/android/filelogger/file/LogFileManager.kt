package abbasi.android.filelogger.file

import abbasi.android.filelogger.config.Constance.Companion.DIRECTORY
import android.content.Context
import java.io.File

internal class LogFileManager(
    context: Context,
    rootDir: String
) {

    private val prefs = context.getSharedPreferences("log_metadata", Context.MODE_PRIVATE)
    private val logsDirectory: String = rootDir + DIRECTORY
    val logFilesList
        get() = File(logsDirectory).listFiles()?.filter { it.isFile }

    init {
        val logDir = File(logsDirectory)
        if (logDir.exists().not()) {
            logDir.mkdirs()
        }
    }

    fun createLogFile(fileName: String): File {
        val logFile = File(logsDirectory, fileName)
        if (logFile.exists()) {
            return logFile
        }

        logFile.createNewFile()
        prefs.edit().putLong(fileName, System.currentTimeMillis()).apply()

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

    fun deleteLogsDir(currentLogPath: String?) {
        File(logsDirectory).listFiles()?.filter {
            it.absolutePath != currentLogPath
        }?.forEach {
            it.delete()
        }
    }
}