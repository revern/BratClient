package com.flatstack.android.main_screen;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.flatstack.android.R;
import com.flatstack.android.utils.Bus;
import com.flatstack.android.utils.ui.BaseDialogFragment;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class TestDialog extends BaseDialogFragment {

    private static final String KEY_TITLE   = "dialogTitle";
    private static final String KEY_ANNOTATIONS = "dialogAnnotations";

    @Bind(R.id.dialog_title)   TextView uiTitle;
    @Bind(R.id.dialog_annotation) EditText uiAnnotation;
    @Bind(R.id.dialog_annotation_list) ListView uiAnnotationList;

    private String title;
    private ArrayList<String> annotationsList;

    @Override public int getLayoutRes() {
        return R.layout.dialog_test;
    }

    public static void show(@Nullable String title,
                            @Nullable ArrayList<String> annotations,
                            @NonNull FragmentManager fm) {

        TestDialog dialog = new TestDialog();

        Bundle args = new Bundle();
        args.putString(KEY_TITLE, title != null ? title : "");
        args.putStringArrayList(KEY_ANNOTATIONS, annotations != null ? annotations : new ArrayList<String>());
        dialog.setArguments(args);

        dialog.show(fm, TestDialog.class.getName());
    }

    @Override protected void parseArguments(@NonNull Bundle args) {
        title = getArguments().getString(KEY_TITLE);
        annotationsList = getArguments().getStringArrayList(KEY_ANNOTATIONS);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        uiTitle.setText(title != null ? title : "");
        ArrayAdapter<String> annotationsAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_view_annotation_item , annotationsList);
        uiAnnotationList.setAdapter(annotationsAdapter);
        uiAnnotationList.setOnItemClickListener((adapterView, v, i, l) -> {
            Bus.event(new AnnotatedEvent(i));
            getDialog().dismiss();
        });
    }
}
