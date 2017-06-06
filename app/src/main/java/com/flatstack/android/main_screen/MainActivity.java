package com.flatstack.android.main_screen;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.flatstack.android.Document;
import com.flatstack.android.utils.ui.BaseActivity;
import com.flatstack.android.R;
import com.flatstack.android.utils.ui.UiInfo;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity {

    @Inject BratInteractor bratInteractor;

    @Bind(R.id.documents_list) ListView uiDocs;

    private ArrayList<String> docNames = new ArrayList<>();
    private List<Document> docs = new ArrayList<>();

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.activity_main)
                .setTitleRes(R.string.app_name)
                .enableBackButton();
    }

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        loadDocumentsList();
    }

    private void showDocs() {
        docNames.clear();
        for (Document doc : docs) {
            docNames.add(doc.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_view_annotation_item, docNames);
        uiDocs.setAdapter(adapter);
    }

    @OnItemClick(R.id.documents_list) public void onDocClick(AdapterView<?> parent,
                                                             View view, int position, long id) {
        startActivity(new Intent(this, AnnotationActivity.class).putExtra(
                AnnotationActivity.ARG_DOCUMENT, docs.get(position)));

    }

    @SuppressLint("SetJavaScriptEnabled") private void loadDocumentsList() {
        bratInteractor.loadDocuments()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    docs = result.getDouments();
                    showDocs();
                }, ignoreError -> {
                });
    }
}