[![](https://jitpack.io/v/aabolfazl/FileLogger.svg)](https://jitpack.io/#aabolfazl/FileLogger)

# FileLogger

The FileLogger is a library for saving logs on Files with custom-formatter on background I/O threads, mobile-ready, android compatible, powered by Java Time library for Android.

## Features

- Write log file on file
- Working on I/O thread
- Using java FastDateTime
- Support INFO, ERROR, DEBUG, WARNING logging level
- Compress and send logs(Email and messengers)
- Startup logs
- Retention Policy (Size, Count, Time to Live)
- Log interception

## TODO
1. Add C++ NDK support
2. Mail logs ✅
3. Upload on http server
4. Encrypt important logs
5. Retrofit/OkHttp Interceptor
6. Startup log ✅

## Usage

**Init:**
```kotlin
val config = Config.Builder(it.path)
    .setDefaultTag("TAG")
    .setLogcatEnable(true)
    .setDataFormatterPattern("dd-MM-yyyy-HH:mm:ss")
    .setRetentionPolicy(RetentionPolicy.TimeToLive(durationInMillis = 1000 * 60 * 10)) // 10 min
    .setStartupData(
        mapOf(
            "App Version" to "${BuildConfig.VERSION}",
            "Device Application Id" to BuildConfig.APPLICATION_ID,
            "Device Version Code" to BuildConfig.VERSION_CODE.toString(),
            "Device Version Name" to BuildConfig.VERSION_NAME,
            "Device Build Type" to BuildConfig.BUILD_TYPE,
            "Device" to Build.DEVICE,
            "Device SDK" to Build.VERSION.SDK_INT.toString(),
            "Device Manufacturer" to Build.MANUFACTURER
        )
    ).build()

FileLogger.init(config)
```
**Log:**
```kotlin
FileLogger.i("TAG", "This is normal Log with custom TAG")
FileLogger.i(msg = "This is normal Info Log")
FileLogger.d(msg = "This is normal Debug Log")
FileLogger.w(msg = "This is normal Warning Log")
FileLogger.e(msg = "This is normal Error Log")
```

**Exception:**
```kotlin
try {
    //...
} catch (e: Exception) {
    FileLogger.e(msg = "log message", throwable = e)
}
```

**Compress to Zip file and Email logs:**
```kotlin
FileLogger.apply {
    compressLogsInZipFile { zipFile ->
        zipFile?.let {
            FileIntent.fromFile(
                this@MainActivity,
                it,
                BuildConfig.APPLICATION_ID
            )?.let { intent ->
                intent.putExtra(Intent.EXTRA_SUBJECT, "File Logs")
                try {
                    startActivity(Intent.createChooser(intent, "Select Email App..."))
                } catch (e: java.lang.Exception) {
                    e(throwable = e)
                }
            }
        }
    }
}
```
for share file with email or etc add this provider in the AndroidManifest.xml file:
```xml
<provider
    android:name="androidx.core.content.FileProvider"
    android:authorities="${applicationId}.provider"
    android:exported="false"
    android:grantUriPermissions="true">
    <meta-data
        android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/provider_paths"/>
</provider>
```
And this one in resource/xml/provider_paths:
```xml
<?xml version="1.0" encoding="utf-8"?>
<paths>
    <external-path name="media" path="."/>
</paths>
```

**Delete log files:**
```kotlin
FileLogger.deleteFiles()
```

**Enable and disable logging:**
```kotlin
FileLogger.setEnable(boolean)
```

## Log Sample
    File logger initialized at 06-03-2022-16:04:51 
    
    06-03-2022-16:05:58 I/Custom: This is normal Log with custom TAG
    06-03-2022-16:05:58 I/TAG: This is normal Info Log
    06-03-2022-16:05:58 D/TAG: This is normal Debug Log
    06-03-2022-16:05:58 W/TAG: This is normal Warning Log
    06-03-2022-16:05:58 E/TAG: This is normal Error Log
    06-03-2022-16:06:00 E/TAG: This is exception
                abbasi.android.filelogger.sample.MainActivity.onCreate$lambda-3(MainActivity.kt:37)
                abbasi.android.filelogger.sample.MainActivity.$r8$lambda$DvDQAirnZLyytJNiMziZSY8Ukuc(Unknown Source:0)
                abbasi.android.filelogger.sample.MainActivity$$ExternalSyntheticLambda0.onClick(Unknown Source:2)
                android.view.View.performClick(View.java:7448)
                com.google.android.material.button.MaterialButton.performClick(MaterialButton.java:1131)
                android.view.View.performClickInternal(View.java:7425)
                android.view.View.access$3600(View.java:810)
                android.view.View$PerformClick.run(View.java:28305)
                android.os.Handler.handleCallback(Handler.java:938)
                android.os.Handler.dispatchMessage(Handler.java:99)
                android.os.Looper.loop(Looper.java:223)
                android.app.ActivityThread.main(ActivityThread.java:7656)
                java.lang.reflect.Method.invoke(Native Method)
                com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:592)
                com.android.internal.os.ZygoteInit.main(ZygoteInit.java:947)


## Installation

Add it in your root build.gradle at the end of repositories:

```gradle
allprojects {
	repositories {
		maven { url 'https://jitpack.io' }
	}
}
```

Add the dependency

```gradle
dependencies { 
    implementation 'com.github.aabolfazl:filelogger:1.0.1'
}
```

License
=======
    MIT License
    Copyright(c) 2022 Abolfazl Abbasi
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    
    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
