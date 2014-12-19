package io.normyle.ui;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.ViewPropertyAnimator;
import android.widget.TextView;

/**
 * Created by MatthewNew on 12/18/2014.
 */
public class StrikerTextView extends TextView {

    public StrikerTextView(Context context) {
        super(context);
    }

    public StrikerTextView(Context context, AttributeSet attrs) {
        super(context,attrs);
    }

    public StrikerTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context,attrs,defStyleAttr);
    }


    public void strikeAnimation(int x) {
        ViewPropertyAnimator animator = this.animate();
        animator.setDuration(1000);
        animator.translationX(x);
        animator.start();
    }

}
