package io.normyle.ui;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.matthew.braggingrights.R;

/**
 * Created by MatthewNew on 12/19/2014.
 */
public class Animations {

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
            View v = inflater.inflate(R.layout.txt_edit_note,ll,true);
            EditText editText = (EditText) v.findViewById(R.id.txt_edit_note_id);
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
