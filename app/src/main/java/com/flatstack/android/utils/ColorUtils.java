package com.flatstack.android.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;

import com.flatstack.android.R;

import java.util.ArrayList;

/**
 * Created by Revern on 06.06.2017.
 */

public class ColorUtils {
    @NonNull public static ArrayList<Integer> getAnntationColors(@NonNull Context context) {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(context.getResources().getColor(R.color.annotation1));
        list.add(context.getResources().getColor(R.color.annotation2));
        list.add(context.getResources().getColor(R.color.annotation3));
        list.add(context.getResources().getColor(R.color.annotation4));
        list.add(context.getResources().getColor(R.color.annotation5));
        list.add(context.getResources().getColor(R.color.annotation6));
        list.add(context.getResources().getColor(R.color.annotation7));
        list.add(context.getResources().getColor(R.color.annotation8));
        list.add(context.getResources().getColor(R.color.annotation9));
        list.add(context.getResources().getColor(R.color.annotation10));
        list.add(context.getResources().getColor(R.color.annotation11));
        list.add(context.getResources().getColor(R.color.annotation12));
        list.add(context.getResources().getColor(R.color.annotation13));
        list.add(context.getResources().getColor(R.color.annotation14));
        list.add(context.getResources().getColor(R.color.annotation15));
        list.add(context.getResources().getColor(R.color.annotation16));
        list.add(context.getResources().getColor(R.color.annotation17));
        list.add(context.getResources().getColor(R.color.annotation18));
        list.add(context.getResources().getColor(R.color.annotation19));
        list.add(context.getResources().getColor(R.color.annotation20));
        return list;
    }
}
