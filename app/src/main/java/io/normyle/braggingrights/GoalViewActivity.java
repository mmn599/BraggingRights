package io.normyle.braggingrights;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import io.matthew.braggingrights.R;
import io.normyle.data.Constants;
import io.normyle.data.Goal;
import io.normyle.data.MySQLiteHelper;
import io.normyle.ui.Animations;
import io.normyle.ui.GoalTypeView;
import io.normyle.ui.NoteView;
import io.normyle.ui.ReminderView;
import io.normyle.ui.TaskView;

public class GoalViewActivity extends ActionBarActivity implements View.OnClickListener,
        TextView.OnEditorActionListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    TextView txtTitle;
    TextView txtStartTime;
    TextView txtEndTime;
    TextView txtDaysInProgress;
    GoalTypeView imgIcon;
    Goal goal;
    LinearLayout llTasks;
    LinearLayout llNotes;
    LinearLayout llReminders;
    Button btnComplete;
    Button btnDelete;
    View oldViewSelected;
    LayoutInflater inflater;
    Activity activity;
    boolean adding_note;
    boolean adding_task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_view);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        activity = this;

        long goal_id = (long) getIntent().getExtras().getInt("GOAL_ID", 0);
        MySQLiteHelper db = new MySQLiteHelper(this);
        goal = db.getGoal((int) goal_id);

        btnComplete = (Button) findViewById(R.id.btn_complete_action_button);
        btnComplete.setOnClickListener(this);

        btnDelete = ((Button) findViewById(R.id.btn_delete_action_button));
        btnDelete.setOnClickListener(this);

        txtTitle = (TextView) findViewById(R.id.txtview_goal_title);
        imgIcon = (GoalTypeView) findViewById(R.id.goaltypeview_goal_icon);
        txtStartTime = (TextView) findViewById(R.id.txtview_start_time);
        txtEndTime = (TextView) findViewById(R.id.txtview_end_time);

        llTasks = (LinearLayout) findViewById(R.id.ll_tasks);
        List<String> taskList = goal.getTaskList();
        for(String task : taskList) {
            TaskView taskView = new TaskView(this, task, this);
            llTasks.addView(taskView);
        }

        inflater = (LayoutInflater)getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        goal.removeOldReminders(new Date());
        db.updateGoal(goal);
        llReminders = (LinearLayout) findViewById(R.id.ll_reminders);
        List<Date> reminderList = goal.getReminderDateList();
        for(Date date : reminderList) {
            ReminderView reminderView = new ReminderView(this,date,this);
            llReminders.addView(reminderView);
        }

        llNotes = (LinearLayout) findViewById(R.id.ll_notes);
        List<String> noteList = goal.getNotesList();
        for(String note : noteList) {
            NoteView noteView = new NoteView(this,note,this);
            llNotes.addView(noteView);
        }

        txtTitle.setText(goal.getTitle());

        HashMap<String, Constants.GoalType> goalTypes = Constants.getGoalTypes();
        Constants.GoalType gt = goalTypes.get(goal.getType());
        imgIcon.setImageResource(gt.getImageId());
        imgIcon.setColor(gt.getColor());

        txtStartTime.setText("Started on: \n" + goal.getStartDateString());
        if(goal.getComplete()==Goal.COMPLETE) {
            txtEndTime.setText("Completed on: \n" + goal.getCompletedDateString());
            btnComplete.setText("Uncomplete Goal");
            txtEndTime.setVisibility(View.VISIBLE);
        }
        else {
            txtEndTime.setVisibility(View.GONE);
        }

        txtDaysInProgress = (TextView) findViewById(R.id.txt_days_in_progress);
        Calendar today = Calendar.getInstance();
        Calendar start = Calendar.getInstance();
        start.setTime(goal.getStartDate());
        int days = 1 + today.get(Calendar.DAY_OF_YEAR) - start.get(Calendar.DAY_OF_YEAR)
        + (today.get(Calendar.YEAR) - start.get(Calendar.YEAR)) * 365;
        String inProgress = days + "\nDays";
        if(days<2) {
            inProgress = inProgress.replace("Days","Day");
        }
        txtDaysInProgress.setText(inProgress);

        db.close();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        //TODO: CHANGE TO REFLECT CHANGE IN OBJECT HIEARCHY OF THESE
        if(v instanceof TaskView || v instanceof NoteView || v instanceof ReminderView) {
            if(oldViewSelected!=null) {
                oldViewSelected.setSelected(false);
            }
            if(v==oldViewSelected) {
                oldViewSelected = null;
                btnComplete.setText("Complete Goal");
                btnDelete.setText("Delete Goal");
            }
            else {
                if(v instanceof TaskView) {
                    btnComplete.setText("Complete Task");
                    btnDelete.setText("Delete Task");
                }
                else if(v instanceof NoteView) {
                    btnComplete.setText("Complete Note");
                    btnDelete.setText("Delete Note");
                }
                else {
                    btnComplete.setText("Complete Goal");
                    btnDelete.setText("Delete Reminder");
                }
                v.setSelected(true);
                oldViewSelected = v;
            }
        }
        else if(id==R.id.btn_complete_action_button) {
            //user wants to complete a task
            if(oldViewSelected!=null) {
                //user wants to complete a task
                if(oldViewSelected instanceof TaskView) {
                    TaskView taskView = (TaskView) oldViewSelected;
                    oldViewSelected.setSelected(false);
                    oldViewSelected = null;
                    String newTaskString = goal.updateTaskReturnNewTask(taskView.getTaskString(), Goal.COMPLETE);
                    MySQLiteHelper.updateGoalInBackground(this, goal);
                    (taskView).completeTaskAnimation(
                            new TaskView.TaskAnimatorListener(llTasks, taskView, newTaskString, this, this));
                }
                btnComplete.setText("Complete Goal");
                btnDelete.setText("Delete Goal");
            }
            //the user wants to complete the goal!
            else {
                if(goal.getComplete()==Goal.INCOMPLETE) {
                    goal.setComplete(Goal.COMPLETE);
                    MySQLiteHelper db = new MySQLiteHelper(this);
                    db.updateGoal(goal);
                    db.close();
                    Toast.makeText(this, "Goal Complete!", Toast.LENGTH_LONG).show();
                    Intent mainActivity = new Intent(this, MainActivity.class);
                    mainActivity.putExtra("WHICH_FRAGMENT", MainActivity.PASTGOALS);
                    startActivity(mainActivity);
                }
                else {
                    goal.setComplete(Goal.INCOMPLETE);
                    MySQLiteHelper db = new MySQLiteHelper(this);
                    db.updateGoal(goal);
                    db.close();
                    Toast.makeText(this, "Goal Uncompleted!", Toast.LENGTH_LONG).show();
                    Intent mainActivity = new Intent(this, MainActivity.class);
                    mainActivity.putExtra("WHICH_FRAGMENT", MainActivity.PRESENTGOALS);
                    startActivity(mainActivity);
                }
            }
        }
        else if(id==0) {
            if(!adding_task) {
                View view = inflater.inflate(R.layout.txt_edit_goal_item, llTasks, true);
                EditText editText = (EditText) view.findViewById(R.id.txt_edit_item_id);
                editText.setTag("TASK");
                editText.setOnEditorActionListener(this);
                editText.setHint("New Task");
                editText.setFocusableInTouchMode(true);
                editText.requestFocus();
                final InputMethodManager inputMethodManager = (InputMethodManager) this
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                adding_task = true;
            }
        }
        else if(id==0) {
            if(!adding_note) {
                View view = inflater.inflate(R.layout.txt_edit_goal_item, llNotes, true);
                final EditText editText = (EditText) view.findViewById(R.id.txt_edit_item_id);
                editText.setTag("NOTE");
                editText.setOnEditorActionListener(this);
                editText.setHint("New Note");
                editText.setFocusableInTouchMode(true);
                editText.requestFocus();
                final InputMethodManager inputMethodManager = (InputMethodManager) this
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                adding_note = true;
            }
        }
        else if(id==0) {
            DatePickerFragment newFragment = new DatePickerFragment();
            newFragment.show(getSupportFragmentManager(), "datePicker");
        }
        else if(id==R.id.btn_delete_action_button) {
            //user wants to delete a note
            if(oldViewSelected instanceof NoteView) {
                NoteView noteView = (NoteView) oldViewSelected;
                oldViewSelected.setSelected(false);
                oldViewSelected = null;
                goal.deleteNote(noteView.getNoteString());
                MySQLiteHelper.updateGoalInBackground(this, goal);
                noteView.deleteNoteAnimation(new Animations.ViewTerminatorListener(llNotes,noteView,this));
            }
            //user wants to delete a reminder
            else if(oldViewSelected instanceof ReminderView) {
                ReminderView reminderView = (ReminderView) oldViewSelected;
                oldViewSelected.setSelected(false);
                oldViewSelected = null;
                goal.deleteReminder(reminderView.getReminderString());
                MySQLiteHelper.updateGoal(this, goal);
                reminderView.deleteNoteAnimation(new Animations.ViewTerminatorListener(llReminders,reminderView,this));
            }
            //user wants to delete a task
            else if(oldViewSelected instanceof TaskView) {
                TaskView taskView = (TaskView) oldViewSelected;
                oldViewSelected.setSelected(false);
                oldViewSelected = null;
                goal.deleteTask(taskView.getTaskString());
                //TODO: debugging to ensure ^^this^^ will always work even if task ahve been changed
                MySQLiteHelper.updateGoal(this, goal);
                taskView.completeTaskAnimation(new Animations.ViewTerminatorListener(llTasks,taskView,this));
            }
            //User wants to delete the goal
            else {
                MySQLiteHelper.deleteGoal(this, goal);
                Toast.makeText(this,"Deleted goal",Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, MainActivity.class));
            }

            btnComplete.setText("Complete Goal");
            btnDelete.setText("Delete Goal");
        }
    }

    private void addNewTask(String newTask, View v) {
        if(newTask.length()>0) {
            goal.addTask(newTask);
            MySQLiteHelper.updateGoal(this,goal);
            llTasks.removeView(v);
            llTasks.addView(new TaskView(this, "0" + newTask, this));
        }
    }

    private void addNewNote(String newNote, View v) {
        if(newNote.length()>0) {
            goal.addNote(newNote);
            MySQLiteHelper.updateGoal(this, goal);
            llNotes.removeView(v);
            llNotes.addView(new NoteView(this, newNote, this));
        }
    }

    private void addNewReminderDate(String note) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month_of_year);
        calendar.set(Calendar.DAY_OF_MONTH, day_of_month);
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        Date newReminderDate = new Date();
        newReminderDate.setTime(calendar.getTimeInMillis());
        MySQLiteHelper db = new MySQLiteHelper(this);
        db.updateGoal(goal);
        db.close();
        Notifications.setOneTimeAlarm(this, calendar, goal.getTitle(), note);
        llReminders.addView(new ReminderView(this, newReminderDate, this));
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            //checks just to be sure
            Object tag = v.getTag();
            if(tag!=null) {
                if(tag instanceof String) {
                    if(tag.equals("TASK")) {
                        addNewTask(v.getText().toString(), v);
                        adding_task = false;
                        return false;
                    }
                    else if(tag.equals("NOTE")) {
                        addNewNote(v.getText().toString(), v);
                        adding_note = false;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private int year;
    private int month_of_year;
    private int day_of_month;
    private int hourOfDay;
    private int minute;

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
        this.hourOfDay = hourOfDay;
        this.minute = minute;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter a note for your reminder: ");
        // Set up the input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String note = input.getText().toString();
                addNewReminderDate(note);
            }
        });
        builder.show();
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
