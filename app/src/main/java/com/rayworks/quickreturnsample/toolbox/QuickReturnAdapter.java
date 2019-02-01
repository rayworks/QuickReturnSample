package com.rayworks.quickreturnsample.toolbox;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.rayworks.quickreturnsample.R;

import java.util.List;

public abstract class QuickReturnAdapter<T> extends BasicRecyclerViewAdapter<T> {
    // footer view type
    // ref:http://stackoverflow.com/questions/26448717/android-5-0-add-header-footer-to-a-
    // recyclerview
    public static final int FOOTER_VIEW = 1;

    // the data ref
    private final List<T> data;
    private boolean deactiveFooter;
    private NavigationListener navigationListener;

    public QuickReturnAdapter(Context context, List<T> list) {
        super(context, list);
        data = list;
    }

    public QuickReturnAdapter<T> setNavigationListener(NavigationListener navigationListener) {
        this.navigationListener = navigationListener;
        return this;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == FOOTER_VIEW) {
            View v =
                    LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.view_footer_list, parent, false);

            SmartFooterViewHolder vh = new SmartFooterViewHolder(mContext, v);

            return vh;
        }

        ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
        onViewHolderItemCreated(viewHolder);

        return viewHolder;
    }

    protected void onViewHolderItemCreated(ViewHolder viewHolder) {
    }

    protected abstract void onBindItemView(ViewHolder holder, int position);

    @Override
    protected void onBindItemViewHolder(ViewHolder holder, int position, Object item) {
        // check footer firstly
        if (holder instanceof QuickReturnAdapter.SmartFooterViewHolder) {
            SmartFooterViewHolder vh = (SmartFooterViewHolder) holder;
            vh.setText(
                    R.id.dialogue_continue_button,
                    "Continue");
        } else {
            onBindItemView(holder, position);
        }
    }

    // Now the critical part. You have return the exact item count of your list
    // I've only one footer. So I returned data.size() + 1
    // If you've multiple headers and footers, you've to return total count
    // like, headers.size() + data.size() + footers.size()
    @Override
    public int getItemCount() {
        if (data == null) {
            return 0;
        }

        if (deactiveFooter) {
            return data.size();
        }

        if (data.size() == 0) {
            // Return 1 here to show nothing
            return 1;
        }

        // Add extra view to show the footer view
        return data.size() + 1;
    }

    // @Override
    // public abstract int getItemLayout();

    // Now define getItemViewType of your own.
    @Override
    public int getItemViewType(int position) {
        if (position == data.size() && !deactiveFooter) {
            // This is where we'll add footer.
            return FOOTER_VIEW;
        }

        // by default, the item type will be 0.
        return super.getItemViewType(position);
    }

    @Override
    public void remove(int pos) {
        if (pos == data.size()) { // footer removal
            deactiveFooter = true;
            notifyItemRemoved(pos);
            return;
        }

        super.remove(pos);
    }

    public interface NavigationListener {
        void onNext();
    }

    // Define a view holder for Footer view
    public class SmartFooterViewHolder extends ViewHolder {
        public SmartFooterViewHolder(Context context, View itemView) {
            super(context, itemView);

            Button view = (Button) itemView.findViewById(R.id.dialogue_continue_button);
            view.setText("Continue");

            view.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#0078ff")));
            /*view.getBackground()
                    .setColorFilter(Color.parseColor("#0078ff"), PorterDuff.Mode.MULTIPLY);*/
            view.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (navigationListener != null) {
                                navigationListener.onNext();
                            }
                        }
                    }
            );
        }
    }
}
