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

public class StaticClass {

    public static void setHighLightedText(TextView tv, String textToHighlight) {
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
}
