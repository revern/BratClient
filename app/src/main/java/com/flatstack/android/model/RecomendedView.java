package com.flatstack.android.model;

import android.support.annotation.NonNull;
import android.widget.TextView;

/**
 * Created by Revern on 13.06.2017.
 */

public class RecomendedView {
    private TextView view;
    private String target;
    private int targetPosition;

    public RecomendedView(@NonNull TextView view, @NonNull String target, int targetPosition) {
        this.view = view;
        this.target = target;
        this.targetPosition = targetPosition;
    }

    public TextView getView() {
        return view;
    }

    public String getTarget() {
        return target;
    }

    public int getTargetPosition() {
        return targetPosition;
    }
}
