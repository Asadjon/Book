package Datas

import android.content.Context
import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import java.io.*

/* 
   The creator of the DatabaseCopyFromAssets class is Asadjon Xusanjonov
   Created on 18:25, 17.02.2021
*/
const val TAG: String = "DatabaseCopyFromAssets"

open class DatabaseCopyFromAssets constructor(private val context: Context, var mFileName: String) {
    var mFilePath: String = context.applicationInfo.dataDir + "/cache/"

    private fun getFileValue(): String {
        val file = File("$mFilePath$mFileName")
        return try {
            val inputStream: InputStream = FileInputStream(file)
            val bytes = ByteArray(inputStream.available())
            inputStream.read(bytes)
            inputStream.close()
            String(bytes)
        } catch (e: IOException) {
            e.printStackTrace()
            "{\"Contents\": []}"
        }
    }

    protected val jsonObject: JSONObject?
        get() {
            return try {
                JSONObject(getFileValue())
            } catch (e: JSONException) {
                e.printStackTrace()
                return null
            }
        }

    protected fun saveJsonObject(jsonObject: JSONObject) {
        val file = File("$mFilePath$mFileName")
        try {
            val fileOutputStream = OutputStreamWriter(FileOutputStream(file))
            fileOutputStream.write(jsonObject.toString())
            fileOutputStream.close()

            Log.i(TAG, "$mFilePath$mFileName saved")
        } catch (e: IOException) {
            e.printStackTrace()
            Log.i(TAG, "$mFilePath$mFileName not saved")
        }
    }

    protected fun deleteFile() {
        val file = File("$mFilePath$mFileName")
        if (file.exists()) {
            file.delete()
            Log.i(TAG, "$mFilePath$mFileName deleted")
        } else  Log.i(TAG, "$mFilePath$mFileName not deleted")
    }

    open fun getValue(json: JSONObject, key: String): Any? {
        return try {
            json.get(key)
        } catch (e: JSONException) {
            e.printStackTrace()
            json
        }
    }

    open fun setValue(json: JSONObject, key: String, value: Any?): DatabaseCopyFromAssets? {
        try {
            json.put(key, value)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return this
    }
}