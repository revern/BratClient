package com.flatstack.android.annotation;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flatstack.android.Api;
import com.flatstack.android.BratInteractor;
import com.flatstack.android.model.Annotation;
import com.flatstack.android.R;
import com.flatstack.android.model.Document;
import com.flatstack.android.base_url.BaseUrlActivity;
import com.flatstack.android.model.RecomendedView;
import com.flatstack.android.utils.Bus;
import com.flatstack.android.utils.CacheUtils;
import com.flatstack.android.utils.ColorUtils;
import com.flatstack.android.utils.Lists;
import com.flatstack.android.utils.NetworkCheckingTimer;
import com.flatstack.android.utils.di.Injector;
import com.flatstack.android.utils.network.NetworkUtils;
import com.flatstack.android.utils.ui.BaseActivity;
import com.flatstack.android.utils.ui.SimpleDialog;
import com.flatstack.android.utils.ui.UiInfo;
import com.google.gson.Gson;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Timer;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class AnnotationActivity extends BaseActivity {

    public static final String ARG_ALL_DOCUMENTS = "allDocuments";
    public static final String ARG_DOCUMENT = "document";
    private static final int VIEW_HEIGHT = 60;

    @Inject SharedPreferences prefs;
    @Inject BratInteractor bratInteractor;

    @Bind(R.id.webView) WebView uiWebView;
    @Bind(R.id.annotation_container) RelativeLayout uiMainContainer;
    @Bind(R.id.ok_btn) TextView uiOk;


    TextView uiFocusedTextView;

    private List<TextView> uiTextList = new ArrayList<>();
    private List<TextView> uiSelectedViews = new ArrayList<>();
    private List<RecomendedView> recommendedViews = new ArrayList<>();
    private List<Annotation> allAnnotationList = new ArrayList<>();
    private ArrayList<String> annotationList = new ArrayList<>();
    private List<Integer> annotationsColorList = new ArrayList<>();
    private List<Document> allDocuments = new ArrayList<>();
    private Document doc;
    private String text;
    private boolean multySelectingState;
    private boolean recomendationState;
    private Gson gson = new Gson();

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
        loadAnnotations();
        showText();
        showAutoAnnotationDialog();
        startNetworkChecking();
    }

    private void showAutoAnnotationDialog() {
        SimpleDialog.show(this, "", "Do you want auto annotate text?", "OK",
                this::setRecommendedAnnotations);
    }

    private void startNetworkChecking() {
        Timer timer = new Timer();
        NetworkCheckingTimer networkCheckingTimer = new NetworkCheckingTimer(() -> {
            if (bratInteractor != null && NetworkUtils.checkNetworkConnection(this)) {
                Set<String> cachedAnnotationsSet = prefs.getStringSet(CacheUtils.KEY_ANNOTATION,
                        new HashSet<>());
                prefs.edit().putStringSet(CacheUtils.KEY_ANNOTATION, new HashSet<>());
                for (Iterator<String> i = cachedAnnotationsSet.iterator(); i.hasNext(); ) {
                    String jsonAnnotation = i.next();
                    Annotation annotation = gson.fromJson(jsonAnnotation, Annotation.class);
                    bratInteractor.addAnnotation(annotation)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(result -> {
                                    }
                                    , error -> {
                                        String json = gson.toJson(annotation);
                                        Set<String> annotationSet = prefs.getStringSet(
                                                CacheUtils.KEY_ANNOTATION, new HashSet<>());
                                        annotationSet.add(json);
                                        prefs.edit().putStringSet(CacheUtils.KEY_ANNOTATION, annotationSet);
                                    });
                }
            }
        });
        timer.scheduleAtFixedRate(networkCheckingTimer, 0, 60 * 1000);
    }

    @Override protected void parseArguments(@NonNull Bundle extras) {
        doc = (Document) extras.getSerializable(ARG_DOCUMENT);
        Document[] docs = (Document[]) extras.getSerializable(ARG_ALL_DOCUMENTS);
        if (docs != null) {
            allDocuments = Lists.add(allDocuments, docs);
        }
    }

    private void loadAnnotations() {
        bratInteractor.loadAllAnnotations()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    allAnnotationList = result.getAnnotations();
                    for (Annotation annotation : result.getAnnotations()) {
                        annotationList.add(annotation.getBody());
                    }
                    Set<String> uniqueValues = new HashSet<>();
                    uniqueValues.addAll(annotationList);
                    annotationList.clear();
                    annotationList.addAll(uniqueValues);
                    annotationList.remove(null);
                    for (int i = annotationList.size() - 1; i >= 0; i--) {
                        if (annotationList.get(i).matches("^http.*") ||
                                annotationList.get(i).matches("^Some.*")) {
                            annotationList.remove(i);
                        }
                    }
                }, error -> Log.i("ERROR", error.getMessage()));
    }

    private void showText() {
        text = doc.getText();
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
            textView.setOnClickListener(view -> {
                uiFocusedTextView = textView;
                if (multySelectingState) {
                    uiSelectedViews.add(textView);
                    textView.setBackgroundColor(Color.LTGRAY);
                } else if(recomendationState) {
                    for(int i = 0; i<recommendedViews.size(); i++) {
                        if(recommendedViews.get(i).getView() == textView) {
                            textView.setBackgroundColor(Color.WHITE);
                            recommendedViews.remove(i);
                        }
                    }
                } else {
                    AnnotationDialog.show(textView.getText().toString(), annotationList,
                            getSupportFragmentManager());
                }
            });
            textView.setOnLongClickListener(view -> {
                textView.setBackgroundColor(Color.LTGRAY);
                uiSelectedViews.clear();
                uiSelectedViews.add((TextView) view);
                multySelectingState = true;
                uiOk.setVisibility(View.VISIBLE);
                return false;
            });
        }
        params.addRule(RelativeLayout.BELOW, R.id.title);
        setMargin(params);
        uiTextList.add(textView);
        uiMainContainer.addView(textView, params);
    }

    private void setMargin(RelativeLayout.LayoutParams params) {
        int marginLeft = 0;
        int marginTop = 0;
        for (int i = 0; i < uiTextList.size(); i++) {
            marginLeft += uiTextList.get(i).getLayoutParams().width;
            if (i == uiTextList.size() - 1) {
                if (marginLeft + params.width >= getResources().getDisplayMetrics().widthPixels) {
                    marginLeft = 0;
                    marginTop += VIEW_HEIGHT;
                }
            } else if (marginLeft + uiTextList.get(i + 1).getLayoutParams().width >=
                    getResources().getDisplayMetrics().widthPixels) {
                marginLeft = 0;
                marginTop += VIEW_HEIGHT;
            }
        }
        params.setMargins(marginLeft, marginTop, 0, 0);
    }

    @OnClick(R.id.ok_btn) public void onOkClick(View view) {
        if(multySelectingState) {
            AnnotationDialog.show("", annotationList, getSupportFragmentManager());
        } else if(recomendationState) {
            sendRecommendedAnnotations();
        }
    }

    private void sendRecommendedAnnotations(){
        recomendationState = false;
        uiOk.setVisibility(View.GONE);

        for(RecomendedView recomendedView : recommendedViews) {
            Annotation annotation = new Annotation(annotationList.get(recomendedView.getTargetPosition()),
                    getAnnotationTarget(recomendedView.getView()));
            bratInteractor.addAnnotation(annotation)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> { }, error -> {
                                Toast.makeText(this, "Connection failed, try to send later",
                                        Toast.LENGTH_SHORT).show();
                                String json = gson.toJson(annotation);
                                Set<String> annotationSet = prefs.getStringSet(CacheUtils.KEY_ANNOTATION,
                                        new HashSet<>());
                                annotationSet.add(json);
                                prefs.edit().putStringSet(CacheUtils.KEY_ANNOTATION, annotationSet);
                            });
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(AnnotatedEvent event) {
        if (multySelectingState) {
            multySelectingState = false;
            uiOk.setVisibility(View.GONE);
        } else {
            uiSelectedViews.add(uiFocusedTextView);
        }
        for (TextView tv : uiSelectedViews) {
            Annotation annotation = new Annotation(annotationList.get(event.getI()),
                    getAnnotationTarget(tv));
            bratInteractor.addAnnotation(annotation)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> tv.setBackgroundColor(annotationsColorList.get(event.getI())),
                            error -> {
                                tv.setBackgroundColor(annotationsColorList.get(event.getI()));
                                Toast.makeText(this, "Connection failed, try to send later",
                                        Toast.LENGTH_SHORT).show();
                                String json = gson.toJson(annotation);
                                Set<String> annotationSet = prefs.getStringSet(CacheUtils.KEY_ANNOTATION,
                                        new HashSet<>());
                                annotationSet.add(json);
                                prefs.edit().putStringSet(CacheUtils.KEY_ANNOTATION, annotationSet);
                            });
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(NewAnnotationEvent event) {
        annotationList.add(event.getAnnotation());
        multySelectingState = false;
        uiOk.setVisibility(View.GONE);
        if (uiSelectedViews.isEmpty()) {
            uiSelectedViews.add(uiFocusedTextView);
        }
        for (TextView tv : uiSelectedViews) {
            Annotation annotation = new Annotation(event.getAnnotation(), getAnnotationTarget(tv));
            bratInteractor.addAnnotation(annotation)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> tv.setBackgroundColor(annotationList.size())
                            , error -> {
                                tv.setBackgroundColor(annotationList.size());
                                Toast.makeText(this, "Connection failed, try to send later",
                                        Toast.LENGTH_SHORT).show();
                                String json = gson.toJson(annotation);
                                Set<String> annotationSet = prefs.getStringSet(CacheUtils.KEY_ANNOTATION,
                                        new HashSet<>());
                                annotationSet.add(json);
                                prefs.edit().putStringSet(CacheUtils.KEY_ANNOTATION, annotationSet);
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
        clonedText += text;
        String replaceSymbols = "";
        for (int i = 0; i < view.getText().toString().length(); i++) {
            replaceSymbols += "?";
        }
        for (int i = 1; i < number; i++) {
            clonedText.replaceFirst(".*" + view.getText().toString() + ".*", replaceSymbols);
        }
        int start = text.indexOf(view.getText().toString());
        int end = start + view.getText().toString().length();
        return prefs.getString(BaseUrlActivity.KEY_BASE_URL, Api.BASE_URL)
                + doc.getId() + "#char=" + start + "," + end;
    }

    private void setRecommendedAnnotations() {
        recommendedViews.clear();
        for (Annotation annotation : allAnnotationList) {
            for (TextView wordView : uiTextList) {
                if (annotation.getText().equals(wordView.getText().toString())) {
                    for (int i = 0; i < annotationList.size(); i++) {
                        if (annotationList.get(i).equals(annotation.getTarget())) {
                            wordView.setBackgroundColor(annotationsColorList.get(i));
                            recommendedViews.add(new RecomendedView(wordView,
                                    annotation.getTarget(), i));
                        }
                    }
                }
            }
        }

        if(!recommendedViews.isEmpty()) {
            recomendationState = true;
            uiOk.setVisibility(View.VISIBLE);
        }
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
