package com.rayworks.quickreturnsample.toolbox;

import android.view.View;

public interface OnItemLongClickListener<T> {
    void onItemLongClick(View itemView, int pos, T item);
}
