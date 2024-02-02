package com.good.fast.app.tenthwallpaper.data

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor

object TenthSqL {
    object TaskContract {
        const val DB_NAME = "TASKDB"
        const val DB_VERSION = 1
        const val TABLE_NAME = "tenths"
        const val COL_ID = "_id"
        const val COL_TASK_NAME = "tenth_name"
        const val COL_TENTH_CLOCKVALUE = "tenth_clockvalue"
        const val TENTH_POSITION = "tenth_position"
        const val TENTH_UUID = "tenth_uuid"
    }

    private const val CREATE_TABLE = "CREATE TABLE ${TaskContract.TABLE_NAME} (" +
            "${TaskContract.COL_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
            "${TaskContract.COL_TASK_NAME} TEXT," +
            "${TaskContract.COL_TENTH_CLOCKVALUE} TEXT," +
            "${TaskContract.TENTH_POSITION} INTEGER," +
            "${TaskContract.TENTH_UUID} TEXT);"

    class TaskDbHelper(context: Context) :
        SQLiteOpenHelper(context, TaskContract.DB_NAME, null, TaskContract.DB_VERSION) {

        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(CREATE_TABLE)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS ${TaskContract.TABLE_NAME}")
            onCreate(db)
        }

        fun insertTask(tenth: TenthDataBean): Long {
            val db = writableDatabase
            val values = ContentValues()
            values.put(TaskContract.COL_TASK_NAME, tenth.tenth_name)
            values.put(TaskContract.COL_TENTH_CLOCKVALUE, tenth.tenth_clockvalue)
            values.put(TaskContract.TENTH_POSITION, tenth.tenth_position)
            values.put(TaskContract.TENTH_UUID, tenth.tenth_uuid)
            return db.insert(TaskContract.TABLE_NAME, null, values)
        }

        fun updateTask(tenth: TenthDataBean): Int {
            val db = writableDatabase
            val values = ContentValues()
            values.put(TaskContract.COL_TASK_NAME, tenth.tenth_name)
            values.put(TaskContract.COL_TENTH_CLOCKVALUE, tenth.tenth_clockvalue)
            values.put(TaskContract.TENTH_POSITION, tenth.tenth_position)
            values.put(TaskContract.TENTH_UUID, tenth.tenth_uuid)
            return db.update(
                TaskContract.TABLE_NAME,
                values,
                "${TaskContract.COL_TASK_NAME} = ?",
                arrayOf(tenth.tenth_name)
            )
        }

        @SuppressLint("Range")
        fun readTask(tenthName: String): TenthDataBean? {
            val db = readableDatabase
            val cursor: Cursor? = db.rawQuery(
                "SELECT * FROM ${TaskContract.TABLE_NAME} WHERE ${TaskContract.COL_TASK_NAME} = ?",
                arrayOf(tenthName)
            )
            cursor?.use {
                if (it.moveToFirst()) {
                    val tenthClockvalue =
                        it.getString(it.getColumnIndex(TaskContract.COL_TENTH_CLOCKVALUE))
                    val tenthPosition = it.getInt(it.getColumnIndex(TaskContract.TENTH_POSITION))
                    val tenthUuid = it.getString(it.getColumnIndex(TaskContract.TENTH_UUID))
                    return TenthDataBean(tenthName, tenthClockvalue, tenthPosition, tenthUuid)
                }
            }
            cursor?.close()
            return null
        }
    }
}