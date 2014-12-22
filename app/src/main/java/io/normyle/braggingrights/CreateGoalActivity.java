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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

import io.matthew.braggingrights.R;
import io.normyle.data.Goal;
import io.normyle.data.MySQLiteHelper;

public class CreateGoalActivity extends ActionBarActivity implements View.OnClickListener,TextView.OnEditorActionListener {

    ImageButton btnMind;
    ImageButton btnBody;
    ImageButton btnSpirit;
    EditText txtTitle;
    EditText txtDescription;
    boolean title_in;
    boolean description_in;
    String type;
    String time;
    ArrayList<String> tasks;
    RadioButton radioYears;
    FloatingActionButton btnComplete;


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
                if(description_in) {
                    btnComplete.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnMind = (ImageButton) findViewById(R.id.btn_mind);
        btnBody = (ImageButton) findViewById(R.id.btn_body);
        btnSpirit = (ImageButton) findViewById(R.id.btn_spirit);
        btnMind.setOnClickListener(this);
        btnBody.setOnClickListener(this);
        btnSpirit.setOnClickListener(this);
        btnMind.setSelected(true);
        type = "Mind";

        radioYears = (RadioButton) findViewById(R.id.radio_days);
        radioYears.setChecked(true);
        time = "Days";
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
            if (id == R.id.btn_mind) {
                btnMind.setSelected(true);
                type = "Mind";
            } else if (id == R.id.btn_body) {
                btnBody.setSelected(true);
                type = "Body";
            } else if (id == R.id.btn_spirit) {
                btnSpirit.setSelected(true);
                type = "Spirit";
            }
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
        btnMind.setSelected(false);
        btnBody.setSelected(false);
        btnSpirit.setSelected(false);
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

