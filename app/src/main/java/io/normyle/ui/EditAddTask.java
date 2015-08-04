package io.normyle.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

/**
 * Created by matthew on 8/3/15.
 */
public class EditAddTask extends EditText {

    private AddTaskCallback mCallback;

    public EditAddTask(Context context) {
        super(context);
    }

    public EditAddTask(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setCallback(AddTaskCallback callback) {
        mCallback = callback;
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if(mCallback!=null) {
                mCallback.onCloseButton(this);
            }
        }
        return super.dispatchKeyEvent(event);
    }

}
