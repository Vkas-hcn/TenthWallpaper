package com.good.fast.app.tenthwallpaper.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import com.good.fast.app.tenthwallpaper.base.BaseViewModel
import com.good.fast.app.tenthwallpaper.data.TenthDataBean
import com.good.fast.app.tenthwallpaper.data.TenthSqL
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.net.UnknownHostException

class MainViewModel : BaseViewModel() {
    var readTask: TenthDataBean = TenthDataBean("TenthWall", "", 0, "")

    fun getSqHelp(context: Context): TenthDataBean {
        val dbHelper = TenthSqL.TaskDbHelper(context)
        return dbHelper.readTask("TenthWall") ?: TenthDataBean("TenthDataBean", "", 0, "")
    }


    fun saveDrawableToGallery(
        activity: AppCompatActivity,
        drawableId: Int,
        title: String,
        description: String,
        showDialog: () -> Unit
    ) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        activity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                ) {
                    showDialog()
                } else {
                    ActivityCompat.requestPermissions(
                        activity,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        2000
                    )
                }
                return
            }
        }
        val bitmap = (ContextCompat.getDrawable(activity, drawableId) as BitmapDrawable).bitmap

        val values = ContentValues().apply {
            put(MediaStore.Images.Media.TITLE, title)
            put(MediaStore.Images.Media.DESCRIPTION, description)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }
        }

        val uri =
            activity.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        uri?.let {
            val outputStream: OutputStream? = activity.contentResolver.openOutputStream(it)
            outputStream?.use { stream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                Toast.makeText(activity, "Image saved to gallery", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun cloakMapData(context: Context): Map<String, Any> {
        return mapOf<String, Any>(
            //distinct_id
            "swum" to (getSqHelp(context).tenth_uuid),
            //client_ts
            "dr" to (System.currentTimeMillis()),
            //device_model
            "brisk" to Build.MODEL,
            //bundle_id
            "beckon" to ("com.cute.cattie.wallpaper"),
            //os_version
            "nasty" to Build.VERSION.RELEASE,
            //gaid
            "dorcas" to "",
            //android_id
            "dialogue" to context.getAppId(),
            //os
            "chrome" to "luminous",
            //app_version
            "moreover" to context.getAppVersion(),
        )
    }

    private fun Context.getAppVersion(): String {
        try {
            val packageInfo = this.packageManager.getPackageInfo(this.packageName, 0)

            return packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return "Version information not available"
    }

    @SuppressLint("HardwareIds")
    private fun Context.getAppId(): String {
        return Settings.Secure.getString(
            this.contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }

    fun getBlackList(context: Context) {
        if (getSqHelp(context).tenth_clockvalue.isNotEmpty()) {
            return
        }
        GlobalScope.launch(Dispatchers.IO) {
            getMapData(
                "https://aghast.cutecattiewallpaper.com/jetliner/ceylon/clod",
                cloakMapData(context),
                onNext = {
                    Log.e("TAG", "The blacklist request is successful：$it")
                    readTask.tenth_clockvalue = it
                    TenthSqL.TaskDbHelper(context).updateTask(readTask)
                },
                onError = {
                    retry(it, context)
                })
        }
    }

    private fun retry(it: String, context: Context) {
        GlobalScope.launch(Dispatchers.IO) {
            delay(10003)
            Log.e("TAG", "The blacklist request failed：$it")
            getBlackList(context)
        }
    }

    private fun Map<String, Any>.toQueryString(): String {
        return this.map { (key, value) ->
            "${URLEncoder.encode(key, "UTF-8")}=${URLEncoder.encode(value.toString(), "UTF-8")}"
        }.joinToString("&")
    }

    private fun HttpURLConnection.configureConnection(urlString: String, onError: ErrorCallback) {
        try {
            requestMethod = "GET"
            connectTimeout = 15000
            doInput = true
            doOutput = false
            useCaches = false

            val url = URL(urlString)
            connect()
        } catch (e: Exception) {
            onError("Network error: ${e.message}")
        }

    }

    private fun readResponse(
        connection: HttpURLConnection,
        onSuccess: SuccessCallback,
        onError: ErrorCallback
    ) {
        try {
            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = connection.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                val response = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }
                reader.close()
                inputStream.close()
                onSuccess(response.toString())
            } else {
                onError("HTTP error: $responseCode")
            }
        } catch (e: UnknownHostException) {
            onError("No network connection")
        } catch (e: Exception) {
            onError("Network error: ${e.message}")
        } finally {
            connection.disconnect()
        }
    }

    private fun getMapData(
        url: String,
        map: Map<String, Any>,
        onNext: SuccessCallback,
        onError: ErrorCallback
    ) {
        val queryParameters = map.toQueryString()
        val urlString = if (url.contains("?")) {
            "$url&$queryParameters"
        } else {
            "$url?$queryParameters"
        }

        val connection = (URL(urlString).openConnection() as HttpURLConnection).apply {
            configureConnection(urlString, onError)
        }

        readResponse(connection, onNext, onError)
    }
}

typealias ErrorCallback = (error: String) -> Unit

typealias SuccessCallback = (response: String) -> Unit