package abbasi.android.filelogger.file

import abbasi.android.filelogger.config.RetentionPolicy

internal class RetentionPolicyChecker(
    private val fileManager: LogFileManager,
) {
    operator fun invoke(policy: RetentionPolicy) {
        val logsList = fileManager.logFilesList ?: return
        when (policy) {
            is RetentionPolicy.FileCountLimit -> {
                val logFiles = logsList.sortedBy { it.lastModified() }

                if (logFiles.size > policy.count) {
                    logFiles.take(logFiles.size - policy.count).forEach { file ->
                        fileManager.deleteLogFile(file.name)
                    }
                }
            }

            is RetentionPolicy.FileSizeLimit -> {
                val logFiles = logsList.sortedBy { it.lastModified() }

                var totalSize = logFiles.sumOf { it.length() }
                if (totalSize > policy.sizeInBytes) {
                    logFiles.forEach { file ->
                        if (totalSize <= policy.sizeInBytes) {
                            return
                        }
                        totalSize -= file.length()
                        fileManager.deleteLogFile(file.name)
                    }
                }
            }

            is RetentionPolicy.TimeToLive -> {
                val now = System.currentTimeMillis()

                logsList.forEach { file ->
                    val creationTime = fileManager.getCreationTime(file.name)

                    if (now - creationTime > policy.durationInMillis) {
                        fileManager.deleteLogFile(file.name)
                    }
                }
            }
        }
    }
}