package com.cyberpanterra.book.Datas

import android.annotation.SuppressLint
import android.content.Context
import com.cyberpanterra.book.Interactions.StaticClass
import org.json.JSONArray
import org.json.JSONObject

/* 
    The creator of the FavouriteDatabaseController class is Asadjon Xusanjonov
    Created on 14:27, 23.03.2022
*/

class FavouriteDatabaseController(context: Context, mFileName: String) : DatabaseCopyFromAssets(context, mFileName) {

    var chapterList: MutableList<SimpleChapter>
        get() {
            return StaticClass.getJsonListAt(getValue(mainJsonObject, "Contents") as JSONArray) { chapterJson: JSONObject ->
                val chapter = Chapter(getValue(chapterJson, "Chapter") as Int,
                        getValue(chapterJson, "Name") as String?,
                        getValue(chapterJson, "Value") as String?,
                        Pages(StaticClass.getJsonListAt(getValue(chapterJson, "Pages") as JSONArray) { target: Int? -> target }.toTypedArray()))

                if (chapterJson.has("Themes"))
                    Chapter(chapter).set(StaticClass.getJsonListAt(getValue(chapterJson, "Themes") as JSONArray) { themeJson: JSONObject ->
                        Theme(getValue(themeJson, "Id") as Int,
                                getValue(themeJson, "Index") as String?,
                                getValue(themeJson, "Name") as String?,
                                Pages(StaticClass.getJsonListAt<Int, Int>(getValue(themeJson, "Pages") as JSONArray) { target: Int? -> target }.toTypedArray()))
                    })
                else SimpleChapter(chapter)
            }
        }
        set(chapters) {
            val mainJson = JSONObject().put("Contents", StaticClass.getJsonListAt(chapters){ chapter ->
                val chapterJson = JSONObject()
                        .put("Chapter", chapter.id)
                        .put("Name", chapter.name)
                        .put("Value", chapter.value)
                        .put("Pages", JSONArray().put(chapter.pages.fromPage).put(chapter.pages.toPage))

                if (chapter.javaClass == Chapter::class.java) {
                    chapterJson.put("Themes", StaticClass.getJsonListAt((chapter as Chapter).themeList){
                        JSONObject()
                                .put("Id", it.id)
                                .put("Index", it.name)
                                .put("Name", it.value)
                                .put("Pages", JSONArray().put(it.pages.fromPage).put(it.pages.toPage))
                    })
                }
                chapterJson
            })
            deleteFile()
            saveJsonObject()
            mainJsonObject = mainJson
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