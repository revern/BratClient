package com.flatstack.android.main_screen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.PixelCopy;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flatstack.android.utils.ui.BaseActivity;
import com.flatstack.android.R;
import com.flatstack.android.utils.ui.UiInfo;

public class MainActivity extends BaseActivity {

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.activity_main)
                .setTitleRes(R.string.app_name)
                .enableBackButton();
    }

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public void onChooseTextClick(View view) {
        startActivity(new Intent(this, AnnotationActivity.class));
    }
}