package com.flatstack.android.annotation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.flatstack.android.R;
import com.flatstack.android.utils.Bus;
import com.flatstack.android.utils.ColorUtils;
import com.flatstack.android.utils.ui.BaseDialogFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class AnnotationDialog extends BaseDialogFragment {

    private static final String KEY_TITLE = "dialogTitle";
    private static final String KEY_ANNOTATIONS = "dialogAnnotations";

    @Bind(R.id.dialog_title) TextView uiTitle;
    @Bind(R.id.dialog_annotation) EditText uiAnnotation;
    @Bind(R.id.dialog_annotation_list) ListView uiAnnotationList;

    private String title;
    private ArrayList<String> annotationsList;
    private List<Integer> annotationsColorList;

    @Override public int getLayoutRes() {
        return R.layout.dialog_annotation;
    }

    public static void show(@Nullable String title,
                            @Nullable ArrayList<String> annotations,
                            @NonNull FragmentManager fm) {

        AnnotationDialog dialog = new AnnotationDialog();

        Bundle args = new Bundle();
        args.putString(KEY_TITLE, title != null ? title : "");
        args.putStringArrayList(KEY_ANNOTATIONS, annotations != null ? annotations : new ArrayList<String>());
        dialog.setArguments(args);

        dialog.show(fm, AnnotationDialog.class.getName());
    }

    @Override protected void parseArguments(@NonNull Bundle args) {
        title = getArguments().getString(KEY_TITLE);
        annotationsList = getArguments().getStringArrayList(KEY_ANNOTATIONS);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        annotationsColorList = ColorUtils.getAnntationColors(getContext());
        uiTitle.setText(title != null ? title : "");
        ArrayAdapter<String> annotationsAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_view_annotation_item, annotationsList);
        uiAnnotationList.setAdapter(annotationsAdapter);
        for (int i = 0; i < annotationsList.size(); i++) {
            uiAnnotationList.getChildAt(i).setBackgroundColor(annotationsColorList.get(i));
        }
        uiAnnotationList.setOnItemClickListener((adapterView, v, i, l) -> {
            Bus.event(new AnnotatedEvent(i));
            getDialog().dismiss();
        });
    }

    @OnClick(R.id.ok_btn) public void onOkClick(View view) {
        Bus.event(new NewAnnotationEvent(uiAnnotation.getText().toString()));
        getDialog().dismiss();
    }
}
