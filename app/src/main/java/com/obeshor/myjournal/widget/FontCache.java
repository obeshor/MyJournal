package com.obeshor.myjournal.widget;

import android.content.Context;
import android.graphics.Typeface;

import java.util.Hashtable;

public class FontCache {

    public static final String ROBOTO_REGULAR = "fonts/robotoregular.ttf";
    public static final String ROBOTO_LIGHT = "fonts/robotolight.ttf";
    public static final String ROBOTO_BOLD = "fonts/robotobold.ttf";

    private static Hashtable<String, Typeface> fontCache = new Hashtable<String, Typeface>();

    public static Typeface getTypeface(String name, Context context) {
        Typeface tf = fontCache.get(name);
        if(tf == null) {
            try {
                tf = Typeface.createFromAsset(context.getAssets(), name);
            }
            catch (Exception e) {
                return null;
            }
            fontCache.put(name, tf);
        }
        return tf;
    }
}
