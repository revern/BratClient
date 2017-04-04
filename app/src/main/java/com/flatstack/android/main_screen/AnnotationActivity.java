package com.flatstack.android.main_screen;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flatstack.android.R;
import com.flatstack.android.utils.Bus;
import com.flatstack.android.utils.di.Injector;
import com.flatstack.android.utils.ui.BaseActivity;
import com.flatstack.android.utils.ui.UiInfo;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class AnnotationActivity extends BaseActivity implements View.OnClickListener {

    private static final int VIEW_HEIGHT = 60;

    @Inject BratInteractor bratInteractor;

    RelativeLayout uiMainContainer;
    TextView uiFocusedTextView;

    private List<TextView> uiTextList = new ArrayList<>();
    private ArrayList<String> annotationList;
    private String text;

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
        Bus.subscribe(this);
        Injector.inject(this);

        loadSettings();

        showText();
    }

    private void loadSettings() {
        //on main thread
        text = bratInteractor.loadText();
        annotationList = bratInteractor.loadAnnotations();
    }

    private void showText() {
        String[] words = text.split(" ");

        for (String word : words) {
            if (word.matches(".*[^A-za-z].*")) {
                boolean wordStarted = false;
                int start = -1;
                char[] symbols = word.toCharArray();
                for (int i = 0; i < symbols.length; i++) {
                    if (symbols[i] < 'A' || symbols[i] > 'z') {
                        if (wordStarted) {
                            addTextView(word.substring(start, i));
                            wordStarted = false;
                        }
                        addTextView(String.valueOf(symbols[i]));
                    } else {
                        if (!wordStarted) {
                            wordStarted = true;
                            start = i;
                        } else if (i == symbols.length - 1) {
                            addTextView(word.substring(start, i + 1));
                        }
                    }
                }
            } else {
                addTextView(word);
            }
        }
    }

    private void addTextView(@NonNull String text) {
        TextView textView = new TextView(getApplicationContext());
        textView.setText(text);
        textView.setTextSize(16f);
        textView.setTextColor(Color.BLACK);
        textView.setTypeface(Typeface.MONOSPACE);
        RelativeLayout.LayoutParams params;

        if (text.length() == 1 && text.matches(".*[^A-Za-z].*")){
            textView.setPadding(0, 0, 0, 0);
            params = new RelativeLayout.LayoutParams(19, VIEW_HEIGHT);
        } else {
            textView.setPadding(16, 0, 0, 0);
            params = new RelativeLayout.LayoutParams(text.length() * 19 + 16, VIEW_HEIGHT);
            textView.setOnClickListener(this);
        }
        params.addRule(RelativeLayout.BELOW, R.id.title);
        setMargin(params, text);
        uiTextList.add(textView);
        uiMainContainer.addView(textView, params);
    }

    private void setMargin(RelativeLayout.LayoutParams params, String text) {
        int marginLeft = 0;
        int marginTop = 0;
        for (int i = 0; i < uiTextList.size(); i++) {
            marginLeft += uiTextList.get(i).getLayoutParams().width;
            if (i == uiTextList.size() - 1) {
                if (marginLeft + params.width >= 720) {
                    marginLeft = 0;
                    marginTop += VIEW_HEIGHT;
                }
            } else if (marginLeft + uiTextList.get(i + 1).getLayoutParams().width >= 720) {
                marginLeft = 0;
                marginTop += VIEW_HEIGHT;
            }
        }
        Log.d("TAG", text + " : " + params.width + " left: " + marginLeft + ", top: " + marginTop);
        params.setMargins(marginLeft, marginTop, 0, 0);
    }

    @Override public void onClick(View view) {
        TextView tv = (TextView) view;
        uiFocusedTextView = tv;
        TestDialog.show(tv.getText().toString(), annotationList, getSupportFragmentManager());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(AnnotatedEvent event) {
        switch (event.getI()) {
            case 0:
                uiFocusedTextView.setBackgroundColor(Color.RED);
                break;
            case 1:
                uiFocusedTextView.setBackgroundColor(Color.BLUE);
                break;
            case 2:
                uiFocusedTextView.setBackgroundColor(Color.GREEN);
                break;
            default:
                break;
        }
    }

}
