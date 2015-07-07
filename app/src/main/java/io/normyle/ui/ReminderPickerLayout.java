package io.normyle.ui;

import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TextView;
import io.matthew.braggingrights.R;

import java.util.ArrayList;

/**
 * Created by matthew on 6/11/15.
 */
public class ReminderPickerLayout extends LinearLayout implements View.OnClickListener,
        RadioGroup.OnCheckedChangeListener{

    LinearLayout llDays;
    EditText input;
    ArrayList<TextView> daysList;
    RadioGroup mRadioGroup;
    boolean mRepeating;

    public ReminderPickerLayout(Context context) {
        super(context);

        this.setOrientation(LinearLayout.VERTICAL);

        input = new EditText(context);
        input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        this.addView(input);

        llDays = new LinearLayout(context);
        llDays.setOrientation(LinearLayout.HORIZONTAL);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRadioGroup = (RadioGroup) (inflater
                .inflate(context.getResources()
                        .getLayout(R.layout.reminder_setup_radio_buttons), this, true))
                        .findViewById(R.id.rgroup_reminder_type);
        ((RadioButton)mRadioGroup.findViewById(R.id.rbtn_repeating)).setChecked(true);
        mRadioGroup.setOnCheckedChangeListener(this);
        mRepeating = true;

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
        if(mRepeating) {
            boolean[] ret = new boolean[7];
            int i = 0;
            for (TextView view : daysList) {
                if (view.isSelected()) {
                    ret[i] = true;
                } else {
                    ret[i] = false;
                }
                i++;
            }
            return ret;
        }
        return null;
    }

    public boolean getRepeating() {
        return mRepeating;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if(checkedId==R.id.rbtn_one_time) {
            mRepeating = false;
            llDays.setVisibility(View.GONE);
        }
        else if(checkedId==R.id.rbtn_repeating) {
            mRepeating = true;
            llDays.setVisibility(View.VISIBLE);
        }
    }

    public String getNote() {
        return input.getText().toString();
    }


}
