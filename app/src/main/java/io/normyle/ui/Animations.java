package io.normyle.ui;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.matthew.braggingrights.R;

/**
 * Created by MatthewNew on 12/19/2014.
 */
public class Animations {

    //deletes the view after the animation is done
    public static class ViewTerminatorListener implements Animator.AnimatorListener {

        LinearLayout ll;
        View oldView;
        Activity activity;

        public ViewTerminatorListener(LinearLayout ll, View oldView,
                                    Activity activity) {
            super();
            this.ll = ll;
            this.oldView = oldView;
            this.activity = activity;
        }

        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            ll.removeView(oldView);
            if(ll.getChildCount()==0) {
                TextView view = new TextView(activity);
                view.setTag("INFO");
                view.setText("Click the clipboard to add a goal task.");
                view.setGravity(Gravity.CENTER_VERTICAL);
                ll.addView(view);
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }

    public static class NoteAddButtonAnimatorListener implements Animator.AnimatorListener {

        LinearLayout ll;
        Activity activity;
        View.OnClickListener listener;
        Button oldView;
        TextView.OnEditorActionListener editorListener;

        public NoteAddButtonAnimatorListener(LinearLayout ll, Button oldView,
                                    Activity activity, View.OnClickListener listener,
                                    TextView.OnEditorActionListener editorListener) {
            super();
            this.ll = ll;
            this.activity = activity;
            this.listener = listener;
            this.oldView = oldView;
            this.editorListener = editorListener;
        }

        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            ll.removeView(oldView);
            LayoutInflater inflater = (LayoutInflater)activity.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.txt_edit_goal_item,ll,true);
            EditText editText = (EditText) v.findViewById(R.id.txt_edit_item_id);
            editText.setOnEditorActionListener(editorListener);
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }

}
