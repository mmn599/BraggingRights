package io.normyle.data;

import android.text.SpannableStringBuilder;
import android.text.style.BulletSpan;
import android.text.style.StrikethroughSpan;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Goal implements Serializable {

	/*
	TODO: better equality checking for task/reminders
	 */

    private String goalTitle;
    private String goalType;
    private String goalTime; //days weeks months years
    private Date startDate;
    private Date completeDate;
    private int goal_importance; //1-5
    private int _id;
    private int complete;
    private String goalNotes;

    private int dateOffset;

    private ArrayList<Reminder> goalReminders;
    private ArrayList<Task> goalTasks;
    private ArrayList<Venture> goalVentures;

    public static class Reminder implements Serializable {
        public boolean repeating;
        public String note;
        public boolean[] days;
        public int hour;
        public int minute;
        public int day_of_year;
        public int year;
        public Reminder() {
            days = new boolean[7];
        }

        @Override
        public boolean equals(Object o) {

            if(!(o instanceof Reminder)) {
                return false;
            }

            //TODO: MAKE MORE ROBUST TO ENSURE SIMILAR GOALS AREN'T CONSIDERED EQUAL
            Reminder other = (Reminder) o;
            if(this.note.equals(other.note) && this.repeating==other.repeating
                && this.hour==other.hour && this.minute==other.minute) {
                return true;
            }
            return false;
        }
    }

    public static class Task implements Serializable {
        public int complete;
        public String task;
        public Date completeDate;

        @Override
        public boolean equals(Object o) {
            if(!(o instanceof Task)) {
                return false;
            }
            Task othertask = (Task) o;
            if(task.equals(othertask.task)) {
                return true;
            }
            return false;
        }
    }

    public static class Venture implements Serializable {
        public Date date;
        public Venture(int dateOffset) {
            /* for DB padding only */
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.WEEK_OF_YEAR, calendar.get(Calendar.WEEK_OF_YEAR) - dateOffset);
            date = calendar.getTime();
        }
    }


    public static final SimpleDateFormat dateFormatForReminders =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static final int INCOMPLETE = 0;
    public static final int COMPLETE = 1;

    /**
     * Default constructor, typically used by the database to build a new goal.
     */
    public Goal() {
        this("","","","",0,0);
    }

    public Goal(String goaltitle, String goaldescription, String goaltype,
                String goaltime, int goalimportance, int dateOffset) {
        this.goalTitle = goaltitle;
        this.goalType = goaltype;
        this.goalTime = goaltime;
        this.goal_importance = goalimportance;
        this._id = 0;
        this.complete = 0;
        this.goalNotes = "";


        /* for DB padding only */
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.WEEK_OF_YEAR, calendar.get(Calendar.WEEK_OF_YEAR) - dateOffset);

        this.startDate = calendar.getTime(); //sets startDate to current time
        this.completeDate = calendar.getTime();

        this.goalReminders = new ArrayList<Reminder>();
        this.goalTasks = new ArrayList<Task>();
        this.goalVentures = new ArrayList<Venture>();
       // this.goalReminders = new ArrayList<Reminder>();

        /* TODO: remove */
        this.dateOffset = dateOffset;
    }

    /**
     * Returns database ID of goal.
     * @return
     */
    public int getId() {
        return _id;
    }

    /**
     * Returns Goal Title.
     * @return
     */
    public String getTitle() {
        return goalTitle;
    }

    /**
     * TODO: change to char or boolean
     * Returns int, 1 for complete 0 for incomplete
     * @return
     */
    public int getComplete() {
        return complete;
    }

    /**
     * Returns type of Goal.
     * @return
     */
    public String getType() {
        return goalType;
    }

    /**
     * Returns the time frame for the goal. (Days, Weeks, Months, Years)
     * @return
     */
    public String getTime() {
        return goalTime;
    }

    /**
     * Returns start date as a pretty string.
     * @return
     */
    public String getStartDateString() {
        return DateFormat.getDateInstance().format(startDate);
    }

    /**
     * Returns start date as a long value.
     * @return
     */
    public long getStartDateLong() {
        return startDate.getTime();
    }

    public Date getStartDate() {
        return startDate;
    }

    /**
     * Returns completed date as a pretty string.
     * @return
     */
    public String getCompletedDateString() {
        return DateFormat.getDateInstance().format(completeDate);
    }

    /**
     * Returns completed date as a pretty string.
     * @return
     */
    public long getCompletedDateLong() {
        return completeDate.getTime();
    }

    public Date getCompleteDate() {
        return completeDate;
    }


    /**
     * Returns unformatted goalNotes.
     * @return
     */
    public String getGoalNotes() {
        return goalNotes;
    }

    /**
     * Returns a list of note strings, breaking on \r (null character)
     * @return
     */
    public List<String> getNotesList() {
        if (goalNotes.length() > 0) {
            String[] noteStrings = goalNotes.split("\r");
            List<String> notesList = new ArrayList<String>();
            for (int i = 0; i < noteStrings.length; i++) {
                notesList.add(noteStrings[i]);
            }
            return notesList;
        }
        else {
            return new ArrayList<String>();
        }
    }

    /**
     * Returns goal importance.
     * @return
     */
    public int getImportance() {
        return goal_importance;
    }

    public void setId(int id) {
        _id=id;
    }

    public void setTitle(String title) {
        goalTitle = title;
    }

    public void setType(String type) {
        goalType = type;
    }

    public void setTime(String time) {
        goalTime = time;
    }

    public void setComplete(int input) {
        if(input==1) {
            /* for DB padding only */
            Calendar date = Calendar.getInstance();
            date.set(Calendar.WEEK_OF_YEAR, date.get(Calendar.WEEK_OF_YEAR) - dateOffset);
            completeDate = date.getTime();
        }
        complete=input;
    }

    public void addNote(String note) {
        goalNotes += note + "\r";
    }

    public void addTask(Task task) {
        goalTasks.add(task);
    }

    public ArrayList<Task> getTasks() {
        return goalTasks;
    }

    public Task updateTask(Task task, int complete) {
        task = goalTasks.get(goalTasks.indexOf(task));
        task.complete = complete;
        if(task.complete==Goal.COMPLETE) {
            /* for DB padding only */
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.WEEK_OF_YEAR, calendar.get(Calendar.WEEK_OF_YEAR) - dateOffset);
            task.completeDate = calendar.getTime();
        }
        return task;
    }

    public void deleteNote(String note) {
        goalNotes = goalNotes.replace(note + "\r", "");
    }

    public void deleteTask(Task task) {
        goalTasks.remove(task);
    }

    public String toString() {
        return ("Goal number "+this._id+"\n"+"Title: "+ goalTitle +"\nDescription: " +"\nType: "+ goalType +
                "\nTime: "+ goalTime +"\nImportance: "+ goal_importance +"\n"+"Complete: "+complete);
    }

    /**
     * This is quite the spaghetti method. Messy code, but it works pretty well for the purposes
     * of formatting the TextView objects for the Task and Notes section of the GoalViewActivity
     *
     * List of strings that compose the tasks for the goal
     * @param list
     * Boolean that indicates the presence of bullets
     * @param bullets
     * Newline indicates whether or not to put newline characters
     * @param newline
     * Indicates whether or not to do strikethrough text
     * @param strikethrough
     * Returns a good 'ol SpannableStringBuilder properly formatted
     * @return
     */
    public static SpannableStringBuilder
        createSpannableString(List<Task> list, boolean bullets,
                              boolean newline, boolean strikethrough) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        int current = 0;
        int location = 0;
        for(Task task : list) {
                boolean incomplete = task.complete!=Goal.COMPLETE;
                String string = task.task;
                builder = builder.append(string);
                if (bullets) {
                    builder.setSpan(new BulletSpan(15), current, current + string.length(), 0);
                }
                if (!incomplete && strikethrough) {
                    builder.setSpan(new StrikethroughSpan(), current, current + string.length(), 0);
                }
                if (newline) {
                    builder.append("\n");
                }
                current += newline ? string.length() + 1 : string.length();
        }
        return builder;
    }

    /**
     * Builds a proper spannable string for a single task, with bullets but without new lines.
     * @param task
     * @return
     */
    public static SpannableStringBuilder createSpannableString(Task task, boolean strikethrough) {
        List<Task> singleTask = new ArrayList<Task>();
        singleTask.add(task);
        return createSpannableString(singleTask, true, false, strikethrough);
    }

    public void addReminder(Reminder reminder) {
        goalReminders.add(reminder);
    }

    public ArrayList<Reminder> getReminder() {
        return goalReminders;
    }

    public void deleteReminder(Reminder reminder) {
        goalReminders.remove(reminder);
    }

    public void setVentures(ArrayList<Venture> ventures) {
        goalVentures = new ArrayList<>();
        for(Venture venture : ventures) {
            goalVentures.add(venture);
        }
    }

    public void addVenture() {
        goalVentures.add(new Venture(dateOffset));
    }

    public ArrayList<Venture> getVentures() {
        return goalVentures;
    }

    public static List<Goal> getGoalsOfType(List<Goal> goals, Constants.GoalType type) {
        List<Goal> relevantGoals = new ArrayList<Goal>();
        for(Goal goal : goals) {
            if(goal.getType().equals(type.getType())) {
                relevantGoals.add(goal);
            }
        }
        return relevantGoals;
    }

    public static List<Venture> getAllVentures(List<Goal> goals) {
        List<Venture> ventures = new ArrayList<Venture>();
        for(Goal goal : goals) {
            for(Venture venture : goal.getVentures()) {
                ventures.add(venture);
            }
        }
        return ventures;
    }


    public static List<Task> getAllCompleteTasks(List<Goal> goals) {
        List<Task> tasks = new ArrayList<Task>();
        for(Goal goal : goals) {
            for(Task task : goal.getTasks()) {
                if(task.complete==Goal.COMPLETE) {
                    tasks.add(task);
                }
            }
        }
        return tasks;
    }

    public static List<Goal> getCompleteGoals(List<Goal> goals) {
        List<Goal> completedGoals = new ArrayList<>();
        for(Goal goal : goals) {
            if(goal.getComplete()==Goal.COMPLETE) {
                completedGoals.add(goal);
            }
        }
        return completedGoals;
    }
}

