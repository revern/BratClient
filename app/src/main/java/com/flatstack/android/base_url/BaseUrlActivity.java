package com.flatstack.android.base_url;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.flatstack.android.Api;
import com.flatstack.android.R;
import com.flatstack.android.documents.DocumentsListActivity;
import com.flatstack.android.utils.di.Injector;
import com.flatstack.android.utils.ui.BaseActivity;
import com.flatstack.android.utils.ui.UiInfo;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnEditorAction;

public class BaseUrlActivity extends BaseActivity {

    public static final String KEY_BASE_URL = "baseUrl";

    @Bind(R.id.base_url) EditText uiBaseUrl;

    @Inject SharedPreferences prefs;

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.activity_settings)
                .setTitleRes(R.string.settings);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.inject(this);

        uiBaseUrl.setText(prefs.getString(KEY_BASE_URL, Api.BASE_URL));
    }

    @OnClick(R.id.url_ok) public void onOkClick() {
        prefs.edit().putString(KEY_BASE_URL, uiBaseUrl.getText().toString());
        startActivity(new Intent(this, DocumentsListActivity.class));
    }

    @OnEditorAction(R.id.base_url) public boolean onActionDone(int actionId){
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            onOkClick();
            return true;
        }
        return false;
    }
}
