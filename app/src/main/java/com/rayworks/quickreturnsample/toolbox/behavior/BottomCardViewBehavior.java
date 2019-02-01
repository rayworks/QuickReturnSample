package com.rayworks.quickreturnsample.toolbox.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by seanzhou on 10/10/16.
 *
 * <p>A custom behavior attached with RecyclerView to achieve parallel scrolling effect.
 */
public class BottomCardViewBehavior extends BottomBehavior implements View.OnTouchListener {

    private RecyclerView recycler;

    private int viewHeight;
    private View bottomView;

    private float downX;
    private float downY;

    private boolean bottomViewDismissed = false;

    public BottomCardViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onNestedPreScroll(
            CoordinatorLayout coordinatorLayout,
            View child,
            View target,
            int dx,
            int dy,
            int[] consumed) {

        bottomView = child;
        viewHeight = bottomView.getHeight();

        if (target instanceof RecyclerView) {
            recycler = (RecyclerView) target;
            recycler.setOnTouchListener(this);
        }
        if (dy > 0 && distance < 0 || dy < 0 && distance > 0) {
            distance = 0;
        }

        distance += dy;

        if (distance > 0 && child.getVisibility() == View.VISIBLE) {
            hide(child);
        } else if (distance < 0 && child.getVisibility() != View.VISIBLE) {
            if (!bottomViewDismissed) show(child);
        }
    }

    private boolean reachFooterZone() {
        if (recycler == null) return false;

        int range = recycler.computeVerticalScrollRange();
        int currentScroll =
                recycler.computeVerticalScrollOffset() + recycler.computeVerticalScrollExtent();

        if (range - currentScroll <= Math.max(viewHeight, bottomView.getHeight())) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float currX = event.getX();
        float currY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = currX;
                downY = currY;

                break;
            case MotionEvent.ACTION_MOVE:
                bottomViewDismissed = false;

                int heightBottomView = Math.max(viewHeight, bottomView.getHeight());

                if (reachFooterZone()) {
                    if (!bottomViewDismissed) bottomViewDismissed = true;

                    if (mAnimator != null) {
                        mAnimator.cancel();
                    }

                    bottomView.setTranslationY(heightBottomView);
                    bottomView.setVisibility(View.INVISIBLE);
                }

                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                bottomViewDismissed = false;
                break;
        }
        return false;
    }
}
