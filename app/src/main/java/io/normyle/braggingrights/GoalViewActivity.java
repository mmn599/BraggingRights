package io.normyle.braggingrights;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
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

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.matthew.braggingrights.R;
import io.normyle.data.Goal;
import io.normyle.data.MySQLiteHelper;
import io.normyle.ui.NoteView;
import io.normyle.ui.ReminderView;
import io.normyle.ui.TaskView;

public class GoalViewActivity extends ActionBarActivity implements View.OnClickListener,
        TextView.OnEditorActionListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    TextView txtTitle;
    TextView txtStartTime;
    TextView txtEndTime;
    ImageView imgIcon;
    Goal goal;
    LinearLayout llTasks;
    LinearLayout llNotes;
    LinearLayout llReminders;
    Button btnComplete;
    Button btnEdit;
    Button btnDelete;
    View oldViewSelected;
    LayoutInflater inflater;
    ImageButton collapseTasks;
    ImageButton collapseNotes;
    ImageButton collapseReminders;
    boolean collapsed_tasks;
    boolean collapsed_notes;
    boolean collapsed_reminders;
    MySQLiteHelper db;



    //TODO: PERFORMANCE: CHANGE THE WAY GOALS ARE PASSED TO THIS ACTIVITY (BY SERIALIZING GOAL)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_view);

        int goal_id = getIntent().getIntExtra("GOAL_ID",0);
        db = new MySQLiteHelper(this);
        goal = db.getGoal(goal_id);

        btnComplete = (Button) findViewById(R.id.btn_complete_action_button);
        btnComplete.setOnClickListener(this);

        btnEdit = ((Button) findViewById(R.id.btn_edit_action_button));
        btnEdit.setOnClickListener(this);
        btnDelete = ((Button) findViewById(R.id.btn_delete_action_button));
        btnDelete.setOnClickListener(this);

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

        inflater = (LayoutInflater)getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        goal.removeOldReminders(new Date());
        db.updateGoal(goal);
        llReminders = (LinearLayout) findViewById(R.id.ll_reminders);
        List<String> reminderList = goal.getRemindersAsList();
        for(String reminder : reminderList) {
            ReminderView reminderView = new ReminderView(this,reminder,this);
            llReminders.addView(reminderView);
        }
        ImageButton addReminderButton = (ImageButton) findViewById(R.id.btn_add_reminder_id);
        addReminderButton.setOnClickListener(this);

        llNotes = (LinearLayout) findViewById(R.id.ll_notes);
        List<String> noteList = goal.getNotesList();
        for(String note : noteList) {
            NoteView noteView = new NoteView(this,note,this);
            llNotes.addView(noteView);
        }
        ImageButton addButton = (ImageButton) findViewById(R.id.btn_add_note_id);
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
            btnComplete.setVisibility(View.GONE);
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

        //TODO: CHANGE TO REFLECT CHANGE IN OBJECT HIEARCHY OF THESE
        if(v instanceof TaskView || v instanceof NoteView || v instanceof ReminderView) {
            if(oldViewSelected!=null) {
                oldViewSelected.setSelected(false);
            }
            if(v==oldViewSelected) {
                oldViewSelected = null;
            }
            else {
                v.setSelected(true);
                oldViewSelected = v;
            }
            if(v instanceof TaskView) {
                //TODO: update drawable to complete
            }
            else {
                //TODO: update drawable to delete
            }
        }
        else if(v.getId()==R.id.btn_complete_action_button) {
            //user just finished a note TODO: make more specific
            View currentFocus = getCurrentFocus();
            if(currentFocus instanceof EditText) {
                addNewNote(currentFocus);
            }
            //user wants to complete a task, delete a note
            else if(oldViewSelected!=null) {
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
                //user wants to delete a note
                else if(oldViewSelected instanceof NoteView) {
                    NoteView noteView = (NoteView) oldViewSelected;
                    oldViewSelected.setSelected(false);
                    oldViewSelected = null;
                    goal.deleteNote(noteView.getNoteString());
                    MySQLiteHelper.updateGoalInBackground(this, goal);
                    noteView.deleteNoteAnimation(new NoteView.NoteAnimatorListener(llNotes,noteView,this));
                }
                else if(oldViewSelected instanceof ReminderView) {
                    ReminderView reminderView = (ReminderView) oldViewSelected;
                    oldViewSelected.setSelected(false);
                    oldViewSelected = null;
                    goal.deleteReminder(reminderView.getReminderString());
                    MySQLiteHelper.updateGoalInBackground(this, goal);
                    reminderView.deleteNoteAnimation(new NoteView.NoteAnimatorListener(llReminders,reminderView,this));
                }
            }
            //the user wants to complete the goal!
            else {
                goal.setComplete(Goal.COMPLETE);
                db.updateGoal(goal);
                Toast.makeText(this,"Goal Complete!",Toast.LENGTH_LONG).show();
                Intent mainActivity = new Intent(this,MainActivity.class);
                mainActivity.putExtra("WHICH_FRAGMENT",MainActivity.PASTFRAGMENT);
                startActivity(mainActivity);
            }
        }
        else if(v.getId()==R.id.btn_add_note_id) {
            View btnview = inflater.inflate(R.layout.txt_edit_note,llNotes,true);
            EditText editText = (EditText) btnview.findViewById(R.id.txt_edit_note_id);
            editText.setOnEditorActionListener(this);
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
        else if(v.getId()==R.id.btn_edit_action_button) {

        }
        else if(v.getId()==R.id.btn_delete_action_button) {
            db.deleteGoal(goal);
            Toast.makeText(this,"Deleted goal",Toast.LENGTH_LONG).show();
            startActivity(new Intent(this,MainActivity.class));
        }
    }

    private void addNewNote(View currentFocus) {
        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        String newNote = ((EditText)currentFocus).getText().toString();
        if(newNote.length()>0) {
            goal.addNote(newNote);
            MySQLiteHelper db = new MySQLiteHelper(this);
            db.updateGoal(goal);
            llNotes.removeView(getCurrentFocus());
            llNotes.addView(new NoteView(this, newNote, this));
        }
    }

    public void addNewReminderDate(Calendar calendar) {
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        Date newReminderDate = new Date();
        newReminderDate.setTime(calendar.getTimeInMillis());
        String newReminder = goal.addReminderDate(newReminderDate);
        MySQLiteHelper.updateGoalInBackground(this, goal);
        Notifications.setOneTimeAlarm(this,calendar);
        llReminders.addView(new ReminderView(this,newReminder,this));
    }

    @Override
    protected void onStop() {
        super.onStop();
        db.close();
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
