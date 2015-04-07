package io.smartsoft.booster.ui.adapters;

import android.view.View;

public interface OnRecyclerViewItemClickListener<Model> {
    public void onItemClick(View view, Model model, int position);
}