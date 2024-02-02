package com.good.fast.app.tenthwallpaper.data

import android.app.WallpaperManager
import android.content.Context
import android.graphics.BitmapFactory
import com.good.fast.app.tenthwallpaper.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object DataUtils {
    val localImageData = listOf(
        R.drawable.icon_80,
        R.drawable.icon_81,
        R.drawable.icon_82,
        R.drawable.icon_83,
        R.drawable.icon_84,
        R.drawable.icon_88,
        R.drawable.icon_89,
        R.drawable.icon_90,
        R.drawable.icon_91,
        R.drawable.icon_93,
        R.drawable.icon_94,
        R.drawable.icon_95,
        R.drawable.icon_96,
        R.drawable.icon_98,
        R.drawable.icon_99,
    )

    suspend fun setWallFun(
        context: Context,
        imagInt: Int,
        type: Int,
        loadBefore: () -> Unit,
        loadAfter: () -> Unit
    ) {
        withContext(Dispatchers.Main) {
            loadBefore()
        }
        val wallpaperManager = WallpaperManager.getInstance(context)
        val bitmap = BitmapFactory.decodeResource(context.resources, imagInt)
        try {
            wallpaperManager.setBitmap(
                bitmap,
                null,
                false,
                type
            )
            withContext(Dispatchers.Main) {
                loadAfter()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }




}