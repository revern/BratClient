package com.flatstack.android.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Collection;

/**
 * Created by Revern on 04.04.2017.
 */

public class Strings {
    @NonNull public static String listToString(@NonNull Collection<String> strings) {
        String result = "";
        for (String string : strings) {
            result += string + ", ";
        }
        if (!result.isEmpty()) {
            return result.substring(0, result.length() - 2);
        } else {
            return result;
        }
    }

    public static boolean isEmpty(@Nullable String str) {
        return str == null || str.isEmpty();
    }

    public static int getPixels(String text){
        int pixels = 0;

        for(char c : text.toCharArray()){
            pixels+=getCharPixel(c);
        }
        return pixels;
    }

    public static int getCharPixel(char c){
        return c;
    }
}
