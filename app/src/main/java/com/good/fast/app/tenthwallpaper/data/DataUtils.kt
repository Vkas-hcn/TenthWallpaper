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
        R.drawable.icon_89,
        R.drawable.icon_88,
        R.drawable.icon_90,
        R.drawable.icon_91,
        R.drawable.icon_93,
        R.drawable.icon_94,
        R.drawable.icon_95,
        R.drawable.icon_96,
        R.drawable.icon_98,
        R.drawable.icon_99,
        R.drawable.ic_1,
        R.drawable.ic_2,
        R.drawable.ic_3,
        R.drawable.ic_4,
        R.drawable.ic_5,
        R.drawable.ic_6,
        R.drawable.ic_7,
        R.drawable.ic_8,
        R.drawable.ic_9,
        R.drawable.ic_10,
        R.drawable.ic_11,
        R.drawable.ic_12,
        R.drawable.ic_13,
        R.drawable.ic_14,
        R.drawable.ic_15,
        R.drawable.ic_16,
        R.drawable.ic_17,
        R.drawable.ic_18,
        R.drawable.ic_19,
        R.drawable.ic_20,
        R.drawable.ic_21,
        R.drawable.ic_22,
        R.drawable.ic_23,
        R.drawable.ic_24,
        R.drawable.ic_25,
        R.drawable.ic_26,
        R.drawable.ic_27,
        R.drawable.ic_28,
        R.drawable.ic_29,
        R.drawable.ic_30,
        R.drawable.ic_31,
        R.drawable.ic_32,
        R.drawable.ic_33,
        R.drawable.ic_34,
        R.drawable.ic_35,
        R.drawable.ic_36,
        R.drawable.ic_40,
        R.drawable.ic_41,
        R.drawable.ic_42,
        R.drawable.ic_43,
        R.drawable.ic_44,
        R.drawable.ic_45,
        R.drawable.ic_46,
        R.drawable.ic_47,
        R.drawable.ic_48,
        R.drawable.ic_49,
        R.drawable.ic_50,
        R.drawable.ic_51,
        R.drawable.ic_53,
        R.drawable.ic_54,
        R.drawable.ic_55,
        R.drawable.ic_56,
        R.drawable.ic_57,
        R.drawable.ic_58,
        R.drawable.ic_60,
        R.drawable.ic_61,
        R.drawable.ic_101,
        R.drawable.ic_103,
        R.drawable.ic_104,
        R.drawable.ic_105,
        R.drawable.ic_601,
        R.drawable.ic_5859,
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