package com.cyberpanterra.book.Datas

import android.annotation.SuppressLint
import android.content.Context
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*

/* 
    The creator of the ChaptersData class is Asadjon Xusanjonov
    Created on 14:27, 23.03.2022
*/   class FavouriteDatabaseController(context: Context, mFileName: String) : DatabaseCopyFromAssets(context, mFileName) {

    var chapters: List<Chapter> = chapterList

    var chapterList: List<Chapter>
        get() {
            val chaptersJson = getValue(mainJsonObject, "Contents") as JSONArray?
            val chapters: MutableList<Chapter> = ArrayList()
            try {
                for (i in 0 until chaptersJson!!.length()) {
                    val chapterJson = chaptersJson.getJSONObject(i)
                    val themesJson = getValue(chapterJson, "Themes") as JSONArray?
                    val themes: MutableList<Theme> = ArrayList()
                    for (j in 0 until themesJson!!.length()) {
                        val themeJson = themesJson.getJSONObject(j)
                        val pageJson = getValue(themeJson, "Pages") as JSONArray?
                        themes.add(Theme(
                                getValue(themeJson, "Id") as Int,
                                getValue(themeJson, "Index") as String?,
                                getValue(themeJson, "Name") as String?,
                                getValue(themeJson, "IsSerialized") as Boolean,
                                Pages(pageJson!!.getInt(0), pageJson.getInt(1))))
                    }

                    chapters.add(Chapter(
                            getValue(chapterJson, "Chapter") as Int,
                            getValue(chapterJson, "Name") as String?,
                            getValue(chapterJson, "Value") as String?,
                            themes))
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return chapters
        }
        set(chapters) {
            val chaptersJson = JSONArray()
            for (chapter in chapters) {
                val themesJson = JSONArray()
                for (theme in chapter.fullThemes) {
                    val themeJson = JSONObject()
                    setValue(themeJson, "Id", theme.id)
                    setValue(themeJson, "Index", theme.index)
                    setValue(themeJson, "Name", theme.name)
                    setValue(themeJson, "IsSerialized", theme.isSerialized)
                    setValue(themeJson, "Pages", JSONArray().put(theme.pages.fromPage).put(theme.pages.toPage))
                    themesJson.put(themeJson)
                }
                val chapterJson = JSONObject()
                setValue(chapterJson, "Chapter", chapter.id)
                setValue(chapterJson, "Name", chapter.name)
                setValue(chapterJson, "Value", chapter.value)
                setValue(chapterJson, "Themes", themesJson)
                chaptersJson.put(chapterJson)
            }
            try {
                val mainJson = JSONObject()
                setValue(mainJson, "Contents", chaptersJson)
                mainJsonObject = mainJson
                deleteFile()
                saveJsonObject()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

    companion object {
        @SuppressLint("StaticFieldLeak")
        var database: FavouriteDatabaseController? = null
            private set

        @JvmStatic
        fun init(context: Context):  FavouriteDatabaseController {
            if (database == null) {
                database = FavouriteDatabaseController(context, "Favourites.json")
            }
            return database as FavouriteDatabaseController
        }
    }
}