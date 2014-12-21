package io.normyle.braggingrights;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.matthew.braggingrights.R;
import io.normyle.data.Constants;
import io.normyle.data.Goal;
import io.normyle.data.MySQLiteHelper;
import io.normyle.ui.Animations;
import io.normyle.ui.TaskView;

public class GoalViewActivity extends ActionBarActivity implements View.OnClickListener,
        TextView.OnEditorActionListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    TextView txtTitle;
    TextView txtStartTime;
    TextView txtEndTime;
    TextView txtGoalNotes;
    TextView txtReminders;
    ImageView imgIcon;
    Goal goal;
    LinearLayout llTasks;
    LinearLayout llNotes;
    LinearLayout llReminders;
    FloatingActionButton fab;
    View oldTaskSelected;
    LayoutInflater inflater;
    ImageButton collapseTasks;
    ImageButton collapseNotes;
    ImageButton collapseReminders;
    boolean collapsed_tasks;
    boolean collapsed_notes;
    boolean collapsed_reminders;



    //TODO: PERFORMANCE: CHANGE THE WAY GOALS ARE PASSED TO THIS ACTIVITY (BY SERIALIZING GOAL)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_view);

        int goal_id = getIntent().getIntExtra("GOAL_ID",0);
        MySQLiteHelper db = new MySQLiteHelper(this);
        goal = db.getGoal(goal_id);
        db.close();

        fab = (FloatingActionButton) findViewById(R.id.btn_complete_action_button);
        fab.setOnClickListener(this);

        txtTitle = (TextView) findViewById(R.id.txtview_goal_title);
        imgIcon = (ImageView) findViewById(R.id.imgview_goal_icon);
        txtStartTime = (TextView) findViewById(R.id.txtview_start_time);
        txtEndTime = (TextView) findViewById(R.id.txtview_end_time);

        llTasks = (LinearLayout) findViewById(R.id.ll_tasks);
        List<String> taskList = goal.getTaskList();
        for(String task : taskList) {
            TaskView taskView = new TaskView(this,task,this);
            llTasks.addView(taskView);
        }

        llReminders = (LinearLayout) findViewById(R.id.ll_reminders);
        txtReminders = (TextView) findViewById(R.id.txtview_reminders);
        goal.removeOldReminders(Calendar.getInstance().getTime());
        MySQLiteHelper.updateGoalInBackground(this,goal);
        txtReminders.setText(Goal.createSpannableString(goal.getRemindersAsList(), true, true, false));
        Button btnAddReminder = (Button) findViewById(R.id.btn_add_reminder_id);
        btnAddReminder.setOnClickListener(this);

        llNotes = (LinearLayout) findViewById(R.id.ll_notes);
        inflater = (LayoutInflater)getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.note_reminder_text,llNotes,true);
        txtGoalNotes = (TextView)v.findViewById(R.id.txtview_note_contents);
        txtGoalNotes.setText(Goal.createSpannableString(goal.getNotesList(),
                true, true, false));

        View view =  inflater.inflate(R.layout.btn_add_note,llNotes,true);
        Button addButton = (Button) view.findViewById(R.id.btn_add_note_id);
        addButton.setOnClickListener(this);

        txtTitle.setText(goal.getTitle());
        if(goal.getType().equals("Mind")) {
            imgIcon.setImageResource(R.drawable.mind_icon);
        }
        else if(goal.getType().equals("Body")) {
            imgIcon.setImageResource(R.drawable.body_icon);
        }
        else {
            imgIcon.setImageResource(R.drawable.spirit_icon);
        }

        txtStartTime.setText("Started on: \n" + goal.getStartDateString());
        if(goal.getComplete()==Goal.COMPLETE) {
            txtEndTime.setText("Completed on: \n"+goal.getCompletedDateString());
            fab.setVisibility(View.GONE);
        }
        else {
            txtEndTime.setVisibility(View.GONE);
        }

        collapseNotes = (ImageButton) findViewById(R.id.btn_collapse_notes);
        collapseTasks = (ImageButton) findViewById(R.id.btn_collapse_tasks);
        collapseReminders = (ImageButton) findViewById(R.id.btn_collapse_reminders);
        collapseNotes.setOnClickListener(this);
        collapseTasks.setOnClickListener(this);
        collapseReminders.setOnClickListener(this);
        collapsed_notes = false;
        collapsed_tasks = false;
        collapsed_reminders = false;
    }

    @Override
    public void onClick(View v) {
        if(v instanceof TaskView) {
            if(oldTaskSelected !=null) {
                oldTaskSelected.setSelected(false);
            }
            v.setSelected(true);
            oldTaskSelected = v;
        }
        else if(v instanceof FloatingActionButton) {
            //user just finished a note TODO: make more specific
            View currentFocus = getCurrentFocus();
            if(currentFocus instanceof EditText) {
                addNewNote(currentFocus);
            }
            //user wants to complete a task
            else if(oldTaskSelected !=null) {
                TaskView taskView = (TaskView) oldTaskSelected;
                oldTaskSelected.setSelected(false);
                oldTaskSelected = null;
                String newTaskString = goal.updateTaskReturnNewTask(taskView.getTaskString(), Goal.COMPLETE);
                MySQLiteHelper.updateGoalInBackground(this,goal);
                (taskView).completeTaskAnimation(Constants.SCREEN_WIDTH,
                        new TaskView.TaskAnimatorListener(llTasks,taskView,newTaskString,this,this));
            }
        }
        else if(v.getId()==R.id.btn_add_note_id) {
            ViewPropertyAnimator animator = v.animate();
            animator.setListener(
                    new Animations.NoteAddButtonAnimatorListener(llNotes,(Button)v,this,this,this));
            animator.setDuration(400);
            animator.translationX(Constants.SCREEN_WIDTH);
            animator.start();
        }
        else if(v.getId()==R.id.btn_add_reminder_id) {
            DatePickerFragment newFragment = new DatePickerFragment();
            newFragment.show(getSupportFragmentManager(), "datePicker");
        }
        else if(v.getId()==R.id.btn_collapse_notes) {
            if(collapsed_notes) {
                llNotes.setVisibility(View.VISIBLE);
                collapsed_notes = false;
                ((ImageButton)v).setImageResource(R.drawable.decollapse);
            }
            else {
                llNotes.setVisibility(View.GONE);
                collapsed_notes = true;
                ((ImageButton)v).setImageResource(R.drawable.collapse);
            }
        }
        else if(v.getId()==R.id.btn_collapse_tasks) {
            if(collapsed_tasks) {
                llTasks.setVisibility(View.VISIBLE);
                collapsed_tasks = false;
                ((ImageButton)v).setImageResource(R.drawable.decollapse);
            }
            else {
                llTasks.setVisibility(View.GONE);
                collapsed_tasks = true;
                ((ImageButton)v).setImageResource(R.drawable.collapse);
            }
        }
        else if(v.getId()==R.id.btn_collapse_reminders) {
            if(collapsed_reminders) {
                llReminders.setVisibility(View.VISIBLE);
                collapsed_reminders = false;
                ((ImageButton)v).setImageResource(R.drawable.decollapse);
            }
            else {
                llReminders.setVisibility(View.GONE);
                collapsed_reminders = true;
                ((ImageButton)v).setImageResource(R.drawable.collapse);
            }
        }
    }

    private void addNewNote(View currentFocus) {
        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        String newNote = ((EditText)currentFocus).getText().toString();
        goal.addNote(newNote);
        MySQLiteHelper.updateGoalInBackground(this,goal);
        llNotes.removeView(getCurrentFocus());
        View view =  inflater.inflate(R.layout.btn_add_note,llNotes,true);
        view.findViewById(R.id.btn_add_note_id).setOnClickListener(this);
        txtGoalNotes.setText(Goal.createSpannableString(goal.getNotesList(),
                true, true, false));

    }
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        boolean handled = false;
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            addNewNote(v);
            handled = true;
        }
        return handled;
    }

    public void addNewReminderDate(Calendar calendar) {
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        Date newReminderDate = new Date();
        newReminderDate.setTime(calendar.getTimeInMillis());
        goal.addReminderDate(newReminderDate);
        MySQLiteHelper.updateGoalInBackground(this,goal);
        txtReminders.setText(Goal.createSpannableString(goal.getRemindersAsList(), true, true, false));
        Notifications.setOneTimeAlarm(this,calendar);
    }

    private int year;
    private int month_of_year;
    private int day_of_month;

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        TimePickerFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
        this.year = year;
        this.month_of_year = monthOfYear;
        this.day_of_month = dayOfMonth;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month_of_year);
        calendar.set(Calendar.DAY_OF_MONTH, day_of_month);
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        addNewReminderDate(calendar);
    }

    public static class TimePickerFragment extends DialogFragment {


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(),
                    (TimePickerDialog.OnTimeSetListener) getActivity(), hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }
    }


    public static class DatePickerFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(),
                    (DatePickerDialog.OnDateSetListener) getActivity(), year, month, day);
        }
    }

}
