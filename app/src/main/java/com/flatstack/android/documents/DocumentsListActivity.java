package com.flatstack.android.documents;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.flatstack.android.annotation.AnnotationActivity;
import com.flatstack.android.utils.ui.BaseActivity;
import com.flatstack.android.R;
import com.flatstack.android.utils.ui.UiInfo;

import java.util.ArrayList;

public class DocumentsListActivity extends BaseActivity {

    ListView uiDocs;

    ArrayList<String> docs = new ArrayList<>();

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.activity_documents_list)
                .setTitleRes(R.string.app_name)
                .enableBackButton();
    }

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiDocs = (ListView) findViewById(R.id.documents_list);

        fillList();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_view_annotation_item, docs);
        uiDocs.setAdapter(adapter);
        uiDocs.setOnItemClickListener((adapterView, v, i, l) -> {
            onChooseTextClick(v);
        });
    }

    @Override protected void parseArguments(@NonNull Bundle extras) {
        //nothing
    }

    public void fillList(){
        docs.clear();
        docs.add("esp.train-doc-10.txt");
        docs.add("esp.train-doc-101.txt");
        docs.add("esp.train-doc-102.txt");
        docs.add("esp.train-doc-103.txt");
        docs.add("esp.train-doc-11.txt");
        docs.add("esp.train-doc-12.txt");
        docs.add("esp.train-doc-13.txt");
        docs.add("esp.train-doc-14.txt");
        docs.add("esp.train-doc-15.txt");
        docs.add("esp.train-doc-16.txt");
        docs.add("esp.train-doc-17.txt");
        docs.add("esp.train-doc-18.txt");
        docs.add("esp.train-doc-19.txt");
        docs.add("esp.train-doc-20.txt");
        docs.add("esp.train-doc-21.txt");
        docs.add("esp.train-doc-22.txt");
        docs.add("esp.train-doc-23.txt");
        docs.add("esp.train-doc-24.txt");
        docs.add("esp.train-doc-25.txt");
        docs.add("esp.train-doc-26.txt");
        docs.add("esp.train-doc-27.txt");
        docs.add("esp.train-doc-29.txt");
        docs.add("esp.train-doc-28.txt");

    }

    public void onChooseTextClick(View view) {
        startActivity(new Intent(this, AnnotationActivity.class));
//        startActivity(new Intent(this, WebViewActivity.class));
    }
}