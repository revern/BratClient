package com.flatstack.android.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Revern on 10.04.2017.
 */

public class Graph {

    @SerializedName("@graph")
    private List<Document> douments;

    public List<Document> getDouments() {
        return douments;
    }
}
