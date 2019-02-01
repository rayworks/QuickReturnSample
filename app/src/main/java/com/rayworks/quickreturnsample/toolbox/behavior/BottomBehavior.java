package com.rayworks.quickreturnsample.toolbox.behavior;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

public class BottomBehavior extends CoordinatorLayout.Behavior<View> {

    private final long DEFAULT_ANIM_HALF_TIME = 250; // MS
    protected ObjectAnimator mAnimator;
    protected int distance;

    public BottomBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(
            CoordinatorLayout coordinatorLayout,
            View child,
            View directTargetChild,
            View target,
            int nestedScrollAxes) {

        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedPreScroll(
            CoordinatorLayout coordinatorLayout,
            View child,
            View target,
            int dx,
            int dy,
            int[] consumed) {

        if (dy > 0 && distance < 0 || dy < 0 && distance > 0) {
            distance = 0;
        }

        distance += dy;

        if (distance > 0 && child.getVisibility() == View.VISIBLE) {
            hide(child);
        } else if (distance < 0 && child.getVisibility() != View.VISIBLE) {
            // https://stackoverflow.com/questions/41142711/25-1-0-android-support-lib-is-breaking-
            // fab-behavior
            show(child);
        }
    }

    protected void hide(final View view) {
        if (mAnimator != null && mAnimator.isRunning()) {
            return;
        }

        mAnimator = ObjectAnimator.ofFloat(view, "translationY", 0, view.getHeight());
        mAnimator.setDuration(DEFAULT_ANIM_HALF_TIME);

        mAnimator.addListener(
                new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.INVISIBLE);
                    }
                });

        mAnimator.start();
    }

    protected void show(final View view) {
        if (mAnimator != null && mAnimator.isRunning()) {
            return;
        }

        mAnimator = ObjectAnimator.ofFloat(view, "translationY", view.getHeight(), 0);
        mAnimator.setDuration(DEFAULT_ANIM_HALF_TIME);

        mAnimator.addListener(
                new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        view.setVisibility(View.VISIBLE);
                    }
                });

        mAnimator.start();
    }
}
