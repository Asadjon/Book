package com.cyberpanterra.book.Datas

import android.annotation.SuppressLint
import android.content.Context
import com.cyberpanterra.book.Interactions.StaticClass
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*

/* 
    The creator of the ChaptersData class is Asadjon Xusanjonov
    Created on 14:27, 23.03.2022
*/   class FavouriteDatabaseController(context: Context, mFileName: String) : DatabaseCopyFromAssets(context, mFileName) {

    var chapterList: MutableList<SimpleChapter>
        get() {
            val chaptersJson = getValue(mainJsonObject, "Contents") as JSONArray?
            val chapters: MutableList<SimpleChapter> = ArrayList()
            try {
                for (i in 0 until chaptersJson!!.length()) {
                    val chapterJson = chaptersJson.getJSONObject(i)
                    val chapterPageJson = getValue(chapterJson, "Pages") as JSONArray?
                    val pages = Pages(chapterPageJson!!.getInt(0), chapterPageJson.getInt(1))

                    chapters.add(if (chapterJson.has("Themes")) {
                        val themesJson = getValue(chapterJson, "Themes") as JSONArray?
                        val themes: MutableList<Theme> = ArrayList()
                        for (j in 0 until themesJson!!.length()) {
                            val themeJson = themesJson.getJSONObject(j)
                            val pageJson = getValue(themeJson, "Pages") as JSONArray?
                            themes.add(Theme(
                                    getValue(themeJson, "Id") as Int,
                                    getValue(themeJson, "Index") as String?,
                                    getValue(themeJson, "Name") as String?,
                                    Pages(pageJson!!.getInt(0), pageJson.getInt(1))))
                        }
                        Chapter(getValue(chapterJson, "Chapter") as Int,
                                getValue(chapterJson, "Name") as String?,
                                getValue(chapterJson, "Value") as String?,
                                pages).set(themes)

                    } else {
                        SimpleChapter(
                                getValue(chapterJson, "Chapter") as Int,
                                getValue(chapterJson, "Name") as String?,
                                getValue(chapterJson, "Value") as String?,
                                pages)

                    })
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return chapters
        }
        set(chapters) {
            val chaptersJson = JSONArray()
            for (chapter in chapters) {
                val chapterJson = JSONObject()
                setValue(chapterJson, "Chapter", chapter.id)
                setValue(chapterJson, "Name", chapter.name)
                setValue(chapterJson, "Value", chapter.value)
                setValue(chapterJson, "Pages", JSONArray().put(chapter.pages.fromPage).put(chapter.pages.toPage))

                if (chapter.javaClass == Chapter::class.java) {
                    val themesJson = JSONArray()
                    StaticClass.forEach((chapter as Chapter).themeList) {
                        val themeJson = JSONObject()
                        setValue(themeJson, "Id", it.id)
                        setValue(themeJson, "Index", it.name)
                        setValue(themeJson, "Name", it.value)
                        setValue(themeJson, "Pages", JSONArray().put(it.pages.fromPage).put(it.pages.toPage))
                        themesJson.put(themeJson)
                    }
                    setValue(chapterJson, "Themes", themesJson)
                }
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
        fun init(context: Context): FavouriteDatabaseController {
            if (database == null) {
                database = FavouriteDatabaseController(context, "Favourites.json")
            }
            return database as FavouriteDatabaseController
        }
    }
}