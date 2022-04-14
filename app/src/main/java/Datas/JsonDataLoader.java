package Datas;

/* 
    The creator of the JsonDataLoader class is Asadjon Xusanjonov
    Created on 13:11, 23.03.2022
*/

import android.content.Context;
import java.io.IOException;
import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonDataLoader {
    private final Context mContext;
    private final String mFileName;
    private final JSONObject mMainJsonObject;

    public JsonDataLoader(Context context, String fileName){
        mContext = context;
        mFileName = fileName;
        mMainJsonObject = getJsonObject();
    }

    protected JSONObject getMainJsonObject() {
        return mMainJsonObject;
    }

    protected String OpenFile(){
        try {
            InputStream inputStream = mContext.getAssets().open(mFileName);
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            inputStream.close();
            return new String(bytes);
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    protected JSONObject getJsonObject() {
        String json = OpenFile();
        if(json != null)
            try {
                return new JSONObject(json);
            } catch (JSONException e) {
                e.printStackTrace();}
        return null;
    }

    protected <T> T getValue(JSONObject jsObject, String key) { return getValue(jsObject, key, null); }

    protected <T> T getValue(JSONObject jsObject, String key, T defValue) {
        if (jsObject != null) {
            try {
                T value = (T) jsObject.get(key);
                return value;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return defValue;
    }

    protected <T> JSONObject setValue(JSONObject jsObject, String key, T value) {
        if (jsObject != null) try {
            jsObject.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  jsObject;
    }
}
