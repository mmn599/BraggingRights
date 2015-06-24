package io.normyle.ui;

import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by matthew on 6/11/15.
 */
public class ReminderPickerLayout extends LinearLayout implements View.OnClickListener {

    LinearLayout llDays;
    EditText input;
    ArrayList<TextView> daysList;

    public ReminderPickerLayout(Context context) {
        super(context);

        this.setOrientation(LinearLayout.VERTICAL);

        input = new EditText(context);
        input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        this.addView(input);

        llDays = new LinearLayout(context);
        llDays.setOrientation(LinearLayout.HORIZONTAL);

        this.addView(llDays);

        daysList = new ArrayList<TextView>();

        addDayTextViews(context);
    }

    private String[] days = {"S","M","T","W","Th","F","S"};

    private void addDayTextViews(Context context) {

        TableLayout.LayoutParams params =
                new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.WRAP_CONTENT, 1f);

        for(int i=0;i<days.length;i++) {
            TextView day = new TextView(context);
            String str = days[i];
            day.setLayoutParams(params);
            day.setText(str);
            day.setTag(str);
            day.setOnClickListener(this);
            day.setSelected(false);
            day.setTextSize(22);
            day.setGravity(Gravity.CENTER_HORIZONTAL);
            daysList.add(day);
            llDays.addView(day);
        }
    }

    public void onClick(View v) {
        if(v.isSelected()) {
            ((TextView)v).setTextColor(Color.BLACK);
            v.setSelected(false);
        }
        else {
            ((TextView)v).setTextColor(Color.GREEN);
            v.setSelected(true);
        }
    }

    public boolean[] getTextViewSelected() {
        boolean[] ret = new boolean[7];
        int i=0;
        for(TextView view : daysList) {
            if(view.isSelected()) {
                ret[i] = true;
            }
            else {
                ret[i] = false;
            }
            i++;
        }
        return ret;
    }

    public String getNote() {
        return input.getText().toString();
    }


}
