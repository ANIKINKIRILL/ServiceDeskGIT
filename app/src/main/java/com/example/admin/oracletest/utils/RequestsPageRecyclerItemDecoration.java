package com.example.admin.oracletest.utils;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class RequestsPageRecyclerItemDecoration extends RecyclerView.ItemDecoration {

    // Vars
    private int bottomItemOffset;

    public RequestsPageRecyclerItemDecoration(int bottomItemOffset){
        this.bottomItemOffset = bottomItemOffset;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = bottomItemOffset;
    }
}
