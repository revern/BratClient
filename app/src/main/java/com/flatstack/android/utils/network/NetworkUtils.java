package com.flatstack.android.utils.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;

/**
 * Created by Revern on 07.06.2017.
 */

public class NetworkUtils {
    public static boolean checkNetworkConnection(@NonNull Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
