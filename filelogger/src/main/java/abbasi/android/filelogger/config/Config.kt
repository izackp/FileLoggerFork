/*
*
* Copyright (c) 2022 Abolfazl Abbasi
*
* */

package abbasi.android.filelogger.config

import abbasi.android.filelogger.config.Constance.Companion.DEFAULT_PATTERN
import abbasi.android.filelogger.config.Constance.Companion.DEFAULT_TAG
import abbasi.android.filelogger.config.Constance.Companion.LOGCAT_ENABLE
import abbasi.android.filelogger.interceptor.LogInterceptor

class Config private constructor(
    val directory: String,
    val defaultTag: String,
    val logcatEnable: Boolean,
    val dataFormatterPattern: String,
    val startupData: Map<String, String>?,
    val retentionPolicy: RetentionPolicy?,
    val logInterceptor: LogInterceptor?,
    var fileRotationStrategy: FileRotationStrategy,
) {

    class Builder(private val directory: String) {
        private var defaultTag: String = DEFAULT_TAG
        private var logcatEnable: Boolean = LOGCAT_ENABLE
        private var dataFormatterPattern: String = DEFAULT_PATTERN
        private var startupData: Map<String, String>? = null
        private var retentionPolicy: RetentionPolicy? = null
        private var logInterceptor: LogInterceptor? = null
        private var fileRotationStrategy: FileRotationStrategy = FileRotationStrategy.None

        fun setDefaultTag(defaultTag: String) = apply {
            this.defaultTag = defaultTag
        }

        fun setLogcatEnable(logcatEnable: Boolean) = apply {
            this.logcatEnable = logcatEnable
        }

        fun setStartupData(startupData: Map<String, String>?) = apply {
            this.startupData = startupData
        }

        fun setRetentionPolicy(retentionPolicy: RetentionPolicy?) = apply {
            this.retentionPolicy = retentionPolicy
        }

        fun setLogInterceptor(logInterceptor: LogInterceptor?) = apply {
            this.logInterceptor = logInterceptor
        }

        fun setNewFileStrategy(fileRotationStrategy: FileRotationStrategy) = apply {
            this.fileRotationStrategy = fileRotationStrategy
        }

        fun setDataFormatterPattern(pattern: String) = apply {
            this.dataFormatterPattern = pattern.replace("/", "-")
                .replace(" ", "")
                .trim()

            if (pattern.isEmpty().or(pattern.contains("/"))) {
                this.dataFormatterPattern = DEFAULT_PATTERN
            }
        }

        fun build() = Config(
            directory = directory,
            defaultTag = defaultTag,
            logcatEnable = logcatEnable,
            dataFormatterPattern = dataFormatterPattern,
            startupData = startupData,
            retentionPolicy = retentionPolicy,
            logInterceptor = logInterceptor,
            fileRotationStrategy = fileRotationStrategy,
        )
    }
}