/*
*
* Copyright (c) 2024 Abolfazl Abbasi
*
* */

package abbasi.android.filelogger.config

sealed interface RetentionPolicy {
    data class FileCountLimit(val count: Int) : RetentionPolicy
    data class FileSizeLimit(val sizeInBytes: Long) : RetentionPolicy
    data class TimeToLive(val durationInMillis: Long) : RetentionPolicy
}