package com.flatstack.android.main_screen;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flatstack.android.R;
import com.flatstack.android.utils.ui.BaseActivity;
import com.flatstack.android.utils.ui.UiInfo;

import java.util.ArrayList;
import java.util.List;

public class AnnotationActivity extends BaseActivity implements View.OnClickListener {

    RelativeLayout uiMainContainer;
    List<TextView> uiTextList = new ArrayList<>();

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.activity_main)
                .setTitleRes(R.string.app_name)
                .enableBackButton();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annotation);

        uiMainContainer = (RelativeLayout) findViewById(R.id.annotation_container);

        showText("hello @!#'my,!@#ASD,asd! friend");
    }

    private void showText(String text) {
        String[] words = text.split(" ");
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        for (String word : words) {
            if (word.matches("^[^A-za-z].*")) {
                boolean wordStarted = false;
                int start = -1;
                char[] symbols = word.toCharArray();
                for (int i = 0; i < symbols.length; i++) {
                    if (symbols[i] < 'A' || symbols[i] > 'z') {
                        if (wordStarted) {
                            TextView textView = new TextView(getApplicationContext());
                            textView.setText(word.substring(start, i));
                            textView.setOnClickListener(this);
                            params.addRule(RelativeLayout.END_OF, uiTextList.get(uiTextList.size() - 1).getId());
                            uiMainContainer.addView(textView, params);
                            uiTextList.add(textView);
                            wordStarted = false;
                        }
                        TextView textView = new TextView(getApplicationContext());
                        textView.setText(symbols[i] + "");
                        textView.setClickable(false);
                        params.addRule(RelativeLayout.END_OF, uiTextList.get(uiTextList.size() - 1).getId());
                        uiTextList.add(textView);
                        uiMainContainer.addView(textView, params);
                    } else {
                        if (!wordStarted) {
                            wordStarted = true;
                            start = i;
                        } else if (i == symbols.length - 1) {
                            TextView textView = new TextView(getApplicationContext());
                            textView.setText(word.substring(start, i + 1));
                            textView.setOnClickListener(this);
                            params.addRule(RelativeLayout.END_OF, uiTextList.get(uiTextList.size() - 1).getId());
                            uiTextList.add(textView);
                            uiMainContainer.addView(textView, params);
                        }
                    }
                }
            } else {
                TextView textView = new TextView(getApplicationContext());
                textView.setText(word);
                textView.setOnClickListener(this);
                if (words[0].equals(word)) {
                    params.addRule(RelativeLayout.BELOW, R.id.title);
                } else {
                    params.addRule(RelativeLayout.BELOW, uiTextList.get(uiTextList.size() - 1).getId());
                }
                uiTextList.add(textView);
                uiMainContainer.addView(textView, params);
            }
        }
    }

    @Override public void onClick(View view) {
        TextView tv = (TextView) view;
        tv.getText();
        tv.setBackgroundColor(getResources().getColor(R.color.red));
    }
}
