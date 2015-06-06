package io.normyle.braggingrights;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

import io.matthew.braggingrights.R;
import io.normyle.data.Constants;
import io.normyle.data.Goal;
import io.normyle.data.MySQLiteHelper;
import io.normyle.ui.GoalTypeView;

public class CreateGoalActivity extends ActionBarActivity implements View.OnClickListener {

    EditText txtTitle;
    boolean title_in;
    String type;
    String time;
    RadioButton radioYears;
    FloatingActionButton btnComplete;
    LinearLayout llicons;
    ArrayList<Constants.GoalType> goalTypes;
    ArrayList<GoalTypeView> typeViews;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_goal);

        btnComplete = (FloatingActionButton) findViewById(R.id.btn_complete_button);
        btnComplete.setOnClickListener(this);
        btnComplete.setVisibility(View.GONE);

        title_in = false;

        txtTitle = (EditText) findViewById(R.id.txt_goal_title);
        txtTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                title_in = true;
                btnComplete.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        llicons = (LinearLayout) findViewById(R.id.ll_icons);


        /* todo: background */
        goalTypes = new ArrayList<Constants.GoalType>(Constants.getGoalTypes().values());
        typeViews = new ArrayList<GoalTypeView>();
        setupTypes();

        radioYears = (RadioButton) findViewById(R.id.radio_days);
        radioYears.setChecked(true);
        time = "Days";
    }

    public void setupTypes() {

        LinearLayout.LayoutParams buttonLayoutParams =
                new LinearLayout.LayoutParams(180, 170);
        buttonLayoutParams.setMargins(0,0,70,0);

        for(Constants.GoalType goalType : goalTypes) {
            //TODO : performance
            GoalTypeView view = new GoalTypeView(this, null, goalType.getColor());
            view.setImageResource(goalType.getImageId());
            view.setTag(goalType.getType());
            view.setOnClickListener(this);
            view.setColor(goalType.getColor());

            typeViews.add(view);
            llicons.addView(view);

            view.setLayoutParams(buttonLayoutParams);
        }

        /* TODO: add exception for empty categories */
        typeViews.get(0).setSelected(true);
        type = (String) typeViews.get(0).getTag();

    }

    public void onClick(View v) {
        int id = v.getId();
        if(id==R.id.btn_complete_button) {
            String title = txtTitle.getText().toString();
            createGoal(new Goal(title,"",type,time,1));
        }
        else {
            deselectButtons();
            v.setSelected(true);
            type = (String) v.getTag();
        }
    }

    public void onRadioButtonClicked(View v) {
        int id = v.getId();
        if(id==R.id.radio_days) {
            time = "Days";
        }
        else if(id==R.id.radio_weeks) {
            time = "Weeks";
        }
        else if(id==R.id.radio_months) {
            time = "Months";
        }
        else if(id==R.id.radio_years) {
            time = "Years";
        }

    }

    private void deselectButtons() {
        for(GoalTypeView btn : typeViews) {
            btn.setSelected(false);
        }
    }

    private void createGoal(Goal goal) {
        int id =  (int) new MySQLiteHelper(this).addGoal(goal);
        Intent intent = new Intent(this, GoalViewActivity.class);
        Bundle data = new Bundle();
        data.putInt("GOAL_ID", id);
        intent.putExtras(data);
        startActivity(intent);
    }

}

