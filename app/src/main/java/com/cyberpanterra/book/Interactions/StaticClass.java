package com.cyberpanterra.book.Interactions;

/* 
    The creator of the StaticClass class is Asadjon Xusanjonov
    Created on 16:02, 24.03.2022
*/

import android.content.Context;
import android.graphics.Rect;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.cyberpanterra.book.Interfaces.Action;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StaticClass {

    public static void setHighLightedText(TextView tv, String textToHighlight) {
        tv.setText(tv.getText().toString());
        String tvt = tv.getText().toString().toUpperCase().trim();
        Spannable wordToSpan = new SpannableString(tv.getText());

        int ofe = tvt.indexOf(textToHighlight);

        for (int ofs = 0; ofs < tvt.length() && ofe != -1; ofs = ofe + 1) {
            ofe = tvt.indexOf(textToHighlight, ofs);
            if (ofe == -1) break;
            else {
                // set color here
                wordToSpan.setSpan(new BackgroundColorSpan(0xFFFFFF00), ofe, ofe + textToHighlight.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv.setText(wordToSpan, TextView.BufferType.SPANNABLE);
            }
        }
    }

    public static boolean keyboardShown(View rootView) {

        final int softKeyboardHeight = 100;
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        int heightDiff = rootView.getBottom() - r.bottom;
        return heightDiff > softKeyboardHeight * dm.density;
    }

    public static void setShowKeyboard(Context context, View view, boolean isShow){
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(!isShow) inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        else inputMethodManager.showSoftInputFromInputMethod(view.getWindowToken(), 1);
    }

    public static <T> void forEach(List<T> list,  Action.IAction<T> function){
        for(T t1 : list) {
            try { function.call(t1);}
            catch (Exception e) { e.printStackTrace(); }
        }
    }

    public static <T, T2> List<T2> getListAt(List<T> list, Action.IRAction<T, T2> function){
        List<T2> newList = new ArrayList<>();
        for(T t1 : list)
            try { newList.add(function.call(t1)); }
            catch (Exception e) { e.printStackTrace(); }
        return newList;
    }

    public static <T> boolean contains(List<T> list, Action.IRAction<T, Boolean> function){
        for(T t1 : list)
            try { if(function.call(t1)) return true; }
            catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public static <T> List<T> whereAll(List<T> list, Action.IRAction<T, Boolean> function){
        List<T> newList = new ArrayList<>();
        for(T t1 : list)
            try { if(function.call(t1)) newList.add(t1); }
            catch (Exception e) { e.printStackTrace(); }
        return newList;
    }

    public static <T> T first(List<T> list, Action.IRAction<T, Boolean> function){
        for(T t1 : list)
            try { if(function.call(t1)) return t1; }
            catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public static <T> boolean trueAll(List<T> list, Action.IRAction<T, Boolean> function){
        for(T t1 : list)
            try { if(!function.call(t1)) return false; }
            catch (Exception e) { e.printStackTrace(); }
        return true;
    }

    public static  <T, T2> List<T2> getJsonListAt(JSONArray jsonArray, Action.IRAction<T, T2> action){
        List<T2> list = new ArrayList<>();
        if (jsonArray == null || action == null) return list;

        for (int i = 0; i < Objects.requireNonNull(jsonArray).length(); i++)
            try { list.add(action.call((T) jsonArray.get(i))); }
            catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public static  <T, T2 extends JSONObject> JSONArray getJsonListAt(List<T> jsonArray, Action.IRAction<T, T2> action){
        JSONArray list = new JSONArray();
        if (jsonArray == null || action == null) return list;

        forEach(jsonArray, target -> {
            try { list.put(action.call(target)); }
            catch (Exception e) { e.printStackTrace(); }
        });
        return list;
    }
}
