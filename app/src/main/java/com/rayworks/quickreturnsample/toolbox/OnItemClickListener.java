package com.rayworks.quickreturnsample.toolbox;

import android.view.View;

public interface OnItemClickListener<T> {
    void onItemClick(View itemView, int pos, T item);
}
