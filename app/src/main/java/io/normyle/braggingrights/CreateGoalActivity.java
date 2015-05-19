package io.normyle.braggingrights;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

import io.matthew.braggingrights.R;
import io.normyle.data.Constants;
import io.normyle.data.Goal;
import io.normyle.data.MySQLiteHelper;

public class CreateGoalActivity extends ActionBarActivity implements View.OnClickListener,TextView.OnEditorActionListener {

    EditText txtTitle;
    EditText txtDescription;
    boolean title_in;
    boolean description_in;
    String type;
    String time;
    ArrayList<String> tasks;
    RadioButton radioYears;
    FloatingActionButton btnComplete;
    LinearLayout llicons;
    ArrayList<Constants.GoalType> goalTypes;
    ArrayList<ImageButton> typeImageButtons;
    String typeSelected;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_goal);

        btnComplete = (FloatingActionButton) findViewById(R.id.btn_complete_button);
        btnComplete.setOnClickListener(this);
        btnComplete.setVisibility(View.INVISIBLE);

        title_in = false;
        description_in = false;
        tasks = new ArrayList<String>();

        txtTitle = (EditText) findViewById(R.id.txt_goal_title);
        txtDescription = (EditText) findViewById(R.id.txt_goal_description);
        txtDescription.setOnEditorActionListener(this);
        txtTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                title_in = true;
                if (description_in) {
                    btnComplete.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        llicons = (LinearLayout) findViewById(R.id.ll_icons);


        /* todo: background */
        goalTypes = Constants.getGoalTypes();
        typeImageButtons = new ArrayList<ImageButton>();
        setupTypes();

        radioYears = (RadioButton) findViewById(R.id.radio_days);
        radioYears.setChecked(true);
        time = "Days";
    }

    public void setupTypes() {

        LinearLayout.LayoutParams buttonLayoutParams =
                new LinearLayout.LayoutParams(180, 170);
        buttonLayoutParams.setMargins(0,0,50,0);

        for(Constants.GoalType goalType : goalTypes) {
            //TODO : performance
            ImageButton imgbtn = new ImageButton(this);
            imgbtn.setImageResource(goalType.getImageId());
            imgbtn.setTag(goalType.getType());
            imgbtn.setOnClickListener(this);
            imgbtn.setBackground(null);

            typeImageButtons.add(imgbtn);
            llicons.addView(imgbtn);

            imgbtn.setLayoutParams(buttonLayoutParams);
            imgbtn.setScaleType(ImageButton.ScaleType.FIT_XY);
        }

        /* TODO: add exception for empty categories */
        typeImageButtons.get(0).setSelected(true);
        typeSelected = (String) typeImageButtons.get(0).getTag();

    }

    public void onClick(View v) {
        int id = v.getId();
        if(id==R.id.btn_complete_button) {
            String title = txtTitle.getText().toString();
            String description = "";
            for(String string : tasks) {
                description += "0"+string+"\n";
            }
            createGoal(new Goal(title,description,type,time,1));
        }
        else {
            deselectButtons();
            v.setSelected(true);
            typeSelected = (String) v.getTag();
            (new Toast(this)).makeText(this,typeSelected,Toast.LENGTH_LONG).show();
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
        for(ImageButton btn : typeImageButtons) {
            btn.setSelected(false);
        }
    }

    private void createGoal(Goal goal) {
        new MySQLiteHelper(this).addGoal(goal);
        startActivity(new Intent(this,MainActivity.class));
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(v.getText().toString().length()>0) {
            String string = '0' + v.getText().toString();
            tasks.add(string);
            v.setText("");
            description_in = true;
            if(title_in) {
                btnComplete.setVisibility(View.VISIBLE);
            }
            v.setHint("Enter another task, if you wish");
        }
        return true;
    }
}

