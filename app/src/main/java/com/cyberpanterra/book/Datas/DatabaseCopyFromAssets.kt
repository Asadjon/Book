package com.cyberpanterra.book.Datas

import android.content.Context
import android.util.Log
import android.widget.Toast
import org.json.JSONException
import org.json.JSONObject
import java.io.*

/* 
   The creator of the DatabaseCopyFromAssets class is Asadjon Xusanjonov
   Created on 18:25, 17.02.2021
*/ open class DatabaseCopyFromAssets constructor(private val context: Context, var mFileName: String) {
    var mFilePath: String = context.applicationInfo.dataDir + "/cache/"

    protected var mainJsonObject: JSONObject

    private fun Creator() {
        val database = File("$mFilePath$mFileName")
        if (!database.exists())
            try {
                if (CopyAtAssets(context.assets.open(mFileName))) Toast.makeText(context, "Copy database success", Toast.LENGTH_SHORT).show()
                else Toast.makeText(context, "Copy data error", Toast.LENGTH_SHORT).show()
            } catch (e: IOException) { e.printStackTrace() }
    }

    private fun CopyAtAssets(inputStream: InputStream): Boolean {
        return try {
            val outFileName = "$mFilePath$mFileName"
            val outputStream: OutputStream = FileOutputStream(outFileName)
            val buff = ByteArray(inputStream.available())
            var length: Int = inputStream.read(buff)
            while (length > 0) {
                outputStream.write(buff, 0, length)
                length = inputStream.read(buff)
            }
            outputStream.flush()
            outputStream.close()
            Log.i("DatabaseCopyFromAssets", "File copied")
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun OpenFile(): String {
        val file = File("$mFilePath$mFileName")
        return try {
            val inputStream: InputStream = FileInputStream(file)
            val bytes = ByteArray(inputStream.available())
            inputStream.read(bytes)
            inputStream.close()
            String(bytes)
        } catch (e: IOException) {
            e.printStackTrace()
            ""
        }
    }

    protected val jsonObject: JSONObject?
        get() {
            return try {
                JSONObject(OpenFile())
            } catch (e: JSONException) {
                e.printStackTrace()
                return null
            }
        }

    protected fun saveJsonObject() {
        val file = File("$mFilePath$mFileName")
        try {
            val fileOutputStream = OutputStreamWriter(FileOutputStream(file))
            fileOutputStream.write(mainJsonObject.toString())
            fileOutputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    protected fun deleteFile() {
        val file = File("$mFilePath$mFileName")
        if (file.exists()) {
            file.delete()
            Log.i("DatabaseCopyFromAssets", "$mFilePath$mFileName deleted")
        } else  Log.i("DatabaseCopyFromAssets", "$mFilePath$mFileName not deleted")
    }

    init {
        Creator()
        mainJsonObject = jsonObject!!
    }

    open fun getValue(json: JSONObject, key: String): Any? {
        try {
            return json.get(key)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return null
    }

    open fun setValue(json: JSONObject, key: String, value: Any?) {
        try {
            json.put(key, value)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}