package io.normyle.braggingrights;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;

import io.matthew.braggingrights.R;
import io.normyle.data.Constants;
import io.normyle.data.Goal;
import io.normyle.data.MySQLiteHelper;
import io.normyle.ui.GoalTypeAdapter;
import io.normyle.ui.GoalTypeViewer;

public class CreateGoalActivity extends ActionBarActivity implements View.OnClickListener {

    EditText txtTitle;
    boolean title_in;
    String time;
    RadioButton radioYears;
    FloatingActionButton btnComplete;
    GoalTypeViewer mGoalTypeViewer;
    View mSelectedType = null;

    //TODO: remove
    EditText offset;


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

        mGoalTypeViewer = (GoalTypeViewer) findViewById(R.id.viewer_goal_types);
        mGoalTypeViewer.setReclicks(true);

        radioYears = (RadioButton) findViewById(R.id.radio_days);
        radioYears.setChecked(true);
        time = "Days";

        offset = (EditText) findViewById(R.id.txt_offset);
    }

    public void onClick(View v) {
        int id = v.getId();
        if(id==R.id.btn_complete_button) {
            String title = txtTitle.getText().toString();
            createGoal(new Goal(title,"",
                    ((TextView)mSelectedType.findViewById(R.id.txtview_type_description)).getText().toString()
                    ,time,1,Integer.parseInt(offset.getText().toString())));
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

    private void createGoal(Goal goal) {
        int id =  (int) new MySQLiteHelper(this).addGoal(goal);
        Intent intent = new Intent(this, GoalViewActivity.class);
        Bundle data = new Bundle();
        data.putInt("GOAL_ID", id);
        intent.putExtras(data);
        startActivity(intent);
    }

}

