package com.flatstack.android.annotation;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flatstack.android.Api;
import com.flatstack.android.BratInteractor;
import com.flatstack.android.model.Annotation;
import com.flatstack.android.R;
import com.flatstack.android.model.Document;
import com.flatstack.android.settings.SettingsActivity;
import com.flatstack.android.utils.Bus;
import com.flatstack.android.utils.ColorUtils;
import com.flatstack.android.utils.di.Injector;
import com.flatstack.android.utils.ui.BaseActivity;
import com.flatstack.android.utils.ui.UiInfo;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class AnnotationActivity extends BaseActivity implements View.OnClickListener {

    public static final String ARG_DOCUMENT = "document";
    private static final int VIEW_HEIGHT = 60;

    @Inject SharedPreferences prefs;
    @Inject BratInteractor bratInteractor;

    @Bind(R.id.annotation_container) RelativeLayout uiMainContainer;
    @Bind(R.id.webView) WebView uiWebView;
    @Bind(R.id.ok_btn) TextView uiOk;
    TextView uiFocusedTextView;

    private List<TextView> uiTextList = new ArrayList<>();
    private List<TextView> uiSelectedVews = new ArrayList<>();
    private ArrayList<String> annotationList = new ArrayList<>();
    private List<Integer> annotationsColorList = new ArrayList<>();
    private Document doc;
    private String text;
    private boolean multySelectingState;

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.activity_documents_list)
                .setTitleRes(R.string.app_name)
                .enableBackButton();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annotation);
        Bus.subscribe(this);
        Injector.inject(this);
        annotationsColorList = ColorUtils.getAnntationColors(this);
        loadSettings();
    }

    @Override protected void parseArguments(@NonNull Bundle extras) {
        doc = (Document) extras.getSerializable(ARG_DOCUMENT);
    }

    @SuppressLint("SetJavaScriptEnabled") private void loadSettings() {
        uiWebView.getSettings().setJavaScriptEnabled(true);
        uiWebView.addJavascriptInterface(new MyJavaScriptInterface(), "INTERFACE");
        uiWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                view.loadUrl("javascript:window.INTERFACE.processContent(document.getElementsByTagName('body')[0].innerText);");
            }
        });
        uiWebView.loadUrl(prefs.getString(SettingsActivity.KEY_BASE_URL, Api.BASE_URL) + doc.getName());

        bratInteractor.loadAllAnnotations()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    for (Annotation annotation : result.getAnnotations()) {
                        annotationList.add(annotation.getBody());
                    }
                    Set<String> uniqueValues = new HashSet<>();
                    uniqueValues.addAll(annotationList);
                    annotationList.clear();
                    annotationList.addAll(uniqueValues);
                    annotationList.remove(null);
                    for (int i = annotationList.size() - 1; i >= 0; i--) {
                        if (annotationList.get(i).matches("^http.*") || annotationList.get(i).matches("^Some.*")) {
                            annotationList.remove(i);
                        }
                    }
                }, error -> Log.i("ERROR", error.getMessage()));
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

        if (text.length() == 1 && text.matches(".*[^A-Za-z].*")) {
            textView.setPadding(0, 0, 0, 0);
            params = new RelativeLayout.LayoutParams(19, VIEW_HEIGHT);
        } else {
            textView.setPadding(16, 0, 0, 0);
            params = new RelativeLayout.LayoutParams(text.length() * 19 + 16, VIEW_HEIGHT);
            textView.setOnClickListener(this);
            textView.setOnLongClickListener(view -> {
                textView.setBackgroundColor(Color.LTGRAY);
                uiSelectedVews.clear();
                uiSelectedVews.add((TextView) view);
                multySelectingState = true;
                uiOk.setVisibility(View.VISIBLE);
                return false;
            });
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
        if (multySelectingState) {
            uiSelectedVews.add(tv);
            tv.setBackgroundColor(Color.LTGRAY);
        } else {
            AnnotationDialog.show(tv.getText().toString(), annotationList, getSupportFragmentManager());
        }
    }

    @OnClick(R.id.ok_btn) public void onOkClick(View view) {
        multySelectingState = false;
        AnnotationDialog.show("", annotationList, getSupportFragmentManager());
        uiOk.setVisibility(View.GONE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(AnnotatedEvent event) {
        if (uiSelectedVews.isEmpty()) {
            uiSelectedVews.add(uiFocusedTextView);
        }
        for (TextView tv : uiSelectedVews) {
            bratInteractor.addAnnotation(annotationList.get(event.getI()), getAnnotationTarget(tv))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result ->
                                    tv.setBackgroundColor(annotationsColorList.get(event.getI()))
                            , ignoreError -> {
                            });
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(NewAnnotationEvent event) {
        if (uiSelectedVews.isEmpty()) {
            uiSelectedVews.add(uiFocusedTextView);
        }
        for (TextView tv : uiSelectedVews) {
            bratInteractor.addAnnotation(event.getAnnotation(), getAnnotationTarget(tv))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> {
                                tv.setBackgroundColor(annotationList.size());
                                annotationList.add(event.getAnnotation());
                            }
                            , ignoreError -> {
                            });
        }
    }

    private String getAnnotationTarget(TextView view) {
        int number = 0;
        for (TextView tv : uiTextList) {
            if (tv.getText().toString().equals(view.getText().toString())) {
                number++;
                if (tv == view) {
                    break;
                }
            }
        }
        String clonedText = "";
        String replaceSympols = "";
        for (int i = 0; i < view.getText().toString().length(); i++) {
            replaceSympols += "?";
        }
        clonedText += text;
        for (int i = 1; i < number; i++) {
            clonedText.replaceFirst(".*" + view.getText().toString() + ".*", replaceSympols);
        }
        int start = text.indexOf(view.getText().toString());
        int end = start + view.getText().toString().length();
        return prefs.getString(SettingsActivity.KEY_BASE_URL, Api.BASE_URL) + "#char=" + start + "," + end;
    }

    private class MyJavaScriptInterface {
        @SuppressWarnings("unused")

        @JavascriptInterface
        public void processContent(String aContent) {
            text = aContent;
            Log.d("contentNET", aContent);
            runOnUiThread(AnnotationActivity.this::showText);
        }
    }

}
