package io.normyle.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import io.matthew.braggingrights.R;

/**
 * Created by MatthewNew on 12/18/2014.
 */
public class TaskView extends FrameLayout {

    Context context;
    String taskString;

    public TaskView(Context context) {
        super(context);
    }

    public TaskView(Context context,String taskString) {
        super(context);
        this.context = context;
        LayoutInflater inflater = (LayoutInflater)context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.task_listview_row,this,true);
    }

    public TaskView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TaskView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


}
