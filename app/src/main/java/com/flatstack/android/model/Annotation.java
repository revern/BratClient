package com.flatstack.android.model;

import android.support.annotation.NonNull;

/**
 * Created by Revern on 10.04.2017.
 */

public class Annotation {

    private String id;
    private String body;
    private String target;
    private String text;

    public Annotation(@NonNull String body, @NonNull String target) {
        this.body = body;
        this.target = target;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public String getTarget() {
        return target;
    }

    public String getText() {
        return text == null ? "" : text;
    }
}
