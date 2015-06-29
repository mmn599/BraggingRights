package io.normyle.braggingrights;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
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
import io.normyle.ui.ReminderPickerLayout;
import io.normyle.ui.ReminderView;
import io.normyle.ui.TaskView;

public class GoalViewActivity extends ActionBarActivity implements View.OnClickListener,
        TextView.OnEditorActionListener, TimePickerDialog.OnTimeSetListener {

    TextView txtTitle;
    TextView txtStartTime;
    TextView txtEndTime;
    TextView txtDaysInProgress;
    TextView txtDaysInProgress2;
    TextView txtVentures;
    GoalTypeView imgIcon;
    Goal goal;
    LinearLayout llTasks;
   // LinearLayout llNotes;
    LinearLayout llReminders;
    Button btnComplete;
    Button btnDelete;
    View oldViewSelected;
    LayoutInflater inflater;
    Activity activity;
    boolean collapsed_tasks;
    boolean collapsed_notes;
    boolean collapsed_reminders;
    boolean adding_note;
    boolean adding_task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_view);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        activity = this;

        int goal_id = getIntent().getIntExtra("GOAL_ID",0);
        MySQLiteHelper db = new MySQLiteHelper(this);
        goal = db.getGoal(goal_id);

        btnComplete = (Button) findViewById(R.id.btn_complete_action_button);
        btnComplete.setOnClickListener(this);

        btnDelete = ((Button) findViewById(R.id.btn_delete_action_button));
        btnDelete.setOnClickListener(this);

        txtTitle = (TextView) findViewById(R.id.txtview_goal_title);
        txtTitle.setText(goal.getTitle());
        imgIcon = (GoalTypeView) findViewById(R.id.imgview_goal_icon);
        txtStartTime = (TextView) findViewById(R.id.txtview_start_time);
        txtEndTime = (TextView) findViewById(R.id.txtview_end_time);

        llTasks = (LinearLayout) findViewById(R.id.ll_tasks);
        List<Goal.Task> taskList = goal.getTasks();
        if(taskList.size()==0) {
            tasksInit();
        }
        else {
            for (Goal.Task task : taskList) {
                TaskView taskView = new TaskView(this, task, this);
                llTasks.addView(taskView);
            }
        }

        ImageButton btnAddTask = (ImageButton) findViewById(R.id.btn_add_tasks);
        btnAddTask.setOnClickListener(this);

        ImageButton btnAddReminder = (ImageButton) findViewById(R.id.btn_add_reminders);
        btnAddReminder.setOnClickListener(this);

        inflater = (LayoutInflater)getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
    //    goal.removeOldReminders(new Date());
        db.updateGoal(goal);
        llReminders = (LinearLayout) findViewById(R.id.ll_reminders);
        ArrayList<Goal.Reminder> reminders = goal.getReminder();
        if(reminders.size()==0) {
            remindersInit();
        }
        else {
            for(Goal.Reminder reminder : reminders) {
                llReminders.addView(new ReminderView(this, reminder, this));
            }
        }
    //TODO:    List<Date> reminderList = goal.getReminderDateList();
    /*    if(reminderList.size()==0) {
            remindersInit();
        }
        else {
        TODO:     for (Date date : reminderList) {
                boolean[] tmp = {false,false,false,false,false,false,false};
                ReminderView reminderView = new ReminderView(this, 3, 30, "poop", tmp, this);
                llReminders.addView(reminderView);
            }
        } */
        findViewById(R.id.btn_add_reminders).setOnClickListener(this);

    /*    llNotes = (LinearLayout) findViewById(R.id.ll_notes);
        List<String> noteList = goal.getNotesList();
        for(String note : noteList) {
            NoteView noteView = new NoteView(this,note,this);
            llNotes.addView(noteView);
        }
        findViewById(R.id.btn_add_note_id).setOnClickListener(this);*/


        List<Constants.GoalType> goalTypes = Constants.getGoalTypes(this);
        String typeString = goal.getType();
        Constants.GoalType myType = null;
        for(Constants.GoalType type : goalTypes) {
            if(type.getType().equals(typeString)) {
                myType = type;
            }
        }
        if(myType!=null) {
            imgIcon.setImageResource(myType.getImageId());
            imgIcon.setColor(myType.getColor());
        }

        txtStartTime.setText("Started on: \n" + goal.getStartDateString());
        if(goal.getComplete()==Goal.COMPLETE) {
            txtEndTime.setText("Completed on: \n" + goal.getCompletedDateString());
            btnComplete.setText("Uncomplete Goal");
            txtEndTime.setVisibility(View.VISIBLE);
        }
        else {
            txtEndTime.setVisibility(View.GONE);
            txtStartTime.setVisibility(View.GONE);
        }

        txtDaysInProgress = (TextView) findViewById(R.id.txt_days_in_progress);
        Calendar today = Calendar.getInstance();
        Calendar start = Calendar.getInstance();
        start.setTime(goal.getStartDate());
        int days = 1 + today.get(Calendar.DAY_OF_YEAR) - start.get(Calendar.DAY_OF_YEAR)
        + (today.get(Calendar.YEAR) - start.get(Calendar.YEAR)) * 365;
        String inProgress = days + "";
        txtDaysInProgress.setText(inProgress);
//        txtDaysInProgress.setTypeface(Typeface.createFromAsset(getAssets(), "jayadhira.ttf"));
        txtDaysInProgress2 = (TextView) findViewById(R.id.txt_days_in_progress2);
        inProgress = days>1 ? "Days in progress":"Days in progress";
        txtDaysInProgress2.setText(inProgress);


        collapsed_notes = false;
        collapsed_tasks = false;
        collapsed_reminders = false;

        txtVentures = (TextView) findViewById(R.id.txtview_goal_ventures);
        txtVentures.setText("Ventures: " + goal.getVentures().size());

        GoalTypeView gt = (GoalTypeView) findViewById(R.id.imgview_goal_icon);
        gt.setOnClickListener(this);

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

    private void remindersInit() {
        TextView view = new TextView(this);
        view.setTag("INFO");
        view.setGravity(Gravity.CENTER_VERTICAL);
        view.setText("Click the clock to add a goal reminder.");
        view.setOnClickListener(this);
        llReminders.addView(view);
    }

    private void tasksInit() {
        TextView view = new TextView(this);
        view.setTag("INFO");
        view.setText("Click the clipboard to add a goal task.");
        view.setGravity(Gravity.CENTER_VERTICAL);
        view.setOnClickListener(this);
        llTasks.addView(view);
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
                btnComplete.setText("Complete\nGoal");
                btnDelete.setText("Delete\nGoal");
            }
            else {
                if(v instanceof TaskView) {
                    btnComplete.setText("Complete\nTask");
                    btnDelete.setText("Delete\nTask");
                }
                else if(v instanceof NoteView) {
                    btnComplete.setText("Complete\nNote");
                    btnDelete.setText("Delete\nNote");
                }
                else {
                    btnComplete.setText("Complete\nGoal");
                    btnDelete.setText("Delete\nReminder");
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
                    Goal.Task newTask = goal.updateTask(taskView.getTask(), Goal.COMPLETE);
                    (taskView).completeTaskAnimation(
                            new TaskView.TaskAnimatorListener(llTasks, taskView, newTask, this, this));
                    MySQLiteHelper.updateGoal(this, goal);
                }
                btnComplete.setText("Complete\nGoal");
                btnDelete.setText("Delete\nGoal");
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
       else if(id==R.id.btn_add_tasks) {
            if(!adding_task) {
                if(llTasks.getChildCount()==1) {
                    if(llTasks.getChildAt(0).getTag()!=null) {
                        llTasks.removeAllViews();
                    }
                }

                View view = inflater.inflate(R.layout.txt_edit_goal_item, llTasks, true);
                EditText editText = (EditText) view.findViewById(R.id.txt_edit_item_id);
                editText.setTag("TASK");
                editText.setOnEditorActionListener(this);
                editText.setHint("New Task");
                editText.setFocusableInTouchMode(true);
                editText.requestFocus();
                editText.setGravity(Gravity.CENTER_VERTICAL);
                final InputMethodManager inputMethodManager = (InputMethodManager) this
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                adding_task = true;
            }
        }
     /*   else if(id==R.id.btn_add_note_id) {
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
        }*/
        else if(id==R.id.btn_add_reminders) {
            if(llReminders.getChildCount()==1) {
                if(llReminders.getChildAt(0).getTag()!=null) {
                    llReminders.removeAllViews();
                }
            }
            TimePickerFragment newFragment = new TimePickerFragment();
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
        //        noteView.deleteNoteAnimation(new Animations.ViewTerminatorListener(llNotes,noteView,this));
            }
            //user wants to delete a reminder
            else if(oldViewSelected instanceof ReminderView) {
                ReminderView reminderView = (ReminderView) oldViewSelected;
                oldViewSelected.setSelected(false);
                oldViewSelected = null;
                goal.deleteReminder(reminderView.getReminder());
                MySQLiteHelper.updateGoal(this, goal);
                reminderView.deleteNoteAnimation(new Animations.ViewTerminatorListener(
                        llReminders,reminderView,this,"REMINDERS"));
            }
            //user wants to delete a task
            else if(oldViewSelected instanceof TaskView) {
                TaskView taskView = (TaskView) oldViewSelected;
                oldViewSelected.setSelected(false);
                oldViewSelected = null;
                goal.deleteTask(taskView.getTask());
                //TODO: debugging to ensure ^^this^^ will always work even if task ahve been changed
                MySQLiteHelper.updateGoal(this, goal);
                taskView.completeTaskAnimation(new Animations.ViewTerminatorListener(
                        llTasks,taskView,this,"TASKS"));
                if(llTasks.getChildCount()==0) {
                    tasksInit();
                }
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
        else if(v.getId()==R.id.imgview_goal_icon) {
            goal.addVenture();
            txtVentures.setText("Ventures: " + goal.getVentures().size());
            MySQLiteHelper.updateGoal(this,goal);
        }
    }

    private void addNewTask(String taskString, View v) {
        if(taskString.length()>0) {
            llTasks.removeView(v);
            Goal.Task newTask = new Goal.Task();
            newTask.complete = 0;
            newTask.task = taskString;
            goal.addTask(newTask);
            MySQLiteHelper.updateGoal(this, goal);
            llTasks.addView(new TaskView(this, newTask, this));
        }
        else {
            Toast.makeText(this, "Your task must have a name.", Toast.LENGTH_LONG).show();
        }
    }

    private void addNewNote(String newNote, View v) {
        if(newNote.length()>0) {
            goal.addNote(newNote);
            MySQLiteHelper.updateGoal(this, goal);
            /*llNotes.removeView(v);
            llNotes.addView(new NoteView(this, newNote, this));*/
        }
    }

    private void addNewReminderDate(String note, boolean[] days_selected, int hourOfDay, int minute) {
        Goal.Reminder reminder = new Goal.Reminder();
        reminder.repeating = true;
        System.arraycopy(days_selected, 0, reminder.days, 0, 7);
        reminder.hour = hourOfDay;
        reminder.minute = minute;
        reminder.note = note;
        goal.addReminder(reminder);
        MySQLiteHelper.updateGoal(this, goal);
        llReminders.addView(new ReminderView(this, reminder, this));
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

    @Override
    public void onTimeSet(TimePicker view, final int hourOfDay, final int minute) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter a note for your reminder: ");
        // Set up the input
        final ReminderPickerLayout layout = new ReminderPickerLayout(this);
        builder.setView(layout);
        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String note = layout.getNote();
                boolean[] days_selected = layout.getTextViewSelected();
                addNewReminderDate(note, days_selected, hourOfDay, minute);
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


}
