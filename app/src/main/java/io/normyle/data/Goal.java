package io.normyle.data;

import android.text.SpannableStringBuilder;
import android.text.style.BulletSpan;
import android.text.style.StrikethroughSpan;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class Goal {

	/*
	 * TODO
	 * implements goalNotes functionality
	 * maybe MAKE SERIALIZABLE
	 */

    private String goalTitle;
    private String goalDescription;
    private String goalType;
    private String goalTime; //days weeks months years
    private Date startDate;
    private Date completeDate;
    private int goal_importance; //1-5
    private int _id;
    private int complete;
    private String goalNotes;
    private String goalReminderString;

    public static final SimpleDateFormat dateFormatForReminders =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static final int INCOMPLETE = 0;
    public static final int COMPLETE = 1;

    /**
     * Default constructor, typically used by the database to build a new goal.
     */
    public Goal() {
        goalReminderString = "";
    }

    /**
     * String title for the goal.
     * @param goaltitle
     * String description for the goal.
     * @param goaldescription
     * String type of the goal.
     * @param goaltype
     * String time frame for the goal.
     * @param goaltime
     * int importance of the goal, currently not in use.
     * @param goalimportance
     */
    public Goal(String goaltitle, String goaldescription, String goaltype, String goaltime, int goalimportance) {
        this.goalTitle = goaltitle;
        this.goalDescription = goaldescription;
        this.goalType = goaltype;
        this.goalTime = goaltime;
        this.goal_importance = goalimportance;
        this._id = 0;
        this.complete = 0;
        this.goalNotes = "";
        this.startDate = new Date(); //sets startDate to current time
        this.completeDate = new Date();
        this.goalReminderString = "";
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

    /**
     * Returns the formatted task list from goalDescription
     * Newline character indicates a new task.
     * Preceding 1 or 0 indicates task completeness.
     * @return
     */
    public List<String> getTaskList() {
        if(goalDescription.length()>0) {
            String[] list = goalDescription.split("\n");
            List<String> taskList = new ArrayList<String>();
            for (int i = 0; i < list.length; i++) {
                taskList.add(list[i]);
            }
            return taskList;
        }
        return new ArrayList<String>();
    }

    public List<Date> getReminderDateList() {
        if(goalReminderString.length()>0) {
            String[] stringDates = goalReminderString.split("\n");
            List<Date> dateList = new ArrayList<Date>();
            for (int i = 0; i < stringDates.length; i++) {
                try {
                    dateList.add(dateFormatForReminders.parse(stringDates[i]));
                } catch (Exception e) {

                }
            }
            return dateList;
        }
        return new ArrayList<Date>();
    }

    /**
     * Returns unformatted goalDescription.
     * @return
     */
    public String getDescription() {
        return goalDescription;
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

    public String getReminderString() {
        return goalReminderString;
    }

    public List<String> getRemindersAsList() {
        List<String> list = new ArrayList<String>();
        List<Date> dateList = getReminderDateList();
        for(Date date : dateList) {
            list.add(dateFormatForReminders.format(date));
        }
        return list;
    }

    public String addReminderDate(Date date) {
        String dateForInsert = dateFormatForReminders.format(date);
        dateForInsert += "\n";
        goalReminderString += dateForInsert;
        return dateForInsert.replace("\n","");
    }

    public void setId(int id) {
        _id=id;
    }

    public void setTitle(String title) {
        goalTitle = title;
    }

    public void setDescription(String des) {
        goalDescription = des;
    }

    public void setType(String type) {
        goalType = type;
    }

    public void setTime(String time) {
        goalTime = time;
    }

    public void setComplete(int input) {
        if(input==1) {
            completeDate = new Date();
        }
        complete=input;
    }

    public void addNote(String note) {
        goalNotes += note + "\r";
    }

    public void addTask(String task) {
        goalDescription += "0" + task + "\n";
    }

    public void setImportance(int imp) {
        goal_importance = imp;
    }

    public void setGoalNotes(String input) {
        goalNotes = input;
    }

    public void setStartDate(long input) {
        startDate = new Date(input);
    }

    /**
     * Task to update, as a string
     * @param task
     * Update to the task, 1 for complete 0 for incomplete
     * @param complete
     * Returns the new task string
     * @return
     */
    public String updateTaskReturnNewTask(String task, int complete) {
        goalDescription = goalDescription.replace(task,complete+task);
        return complete+task;
    }

    public void deleteNote(String note) {
        goalNotes = goalNotes.replace(note+"\r","");
    }

    public void deleteTask(String task) {
        goalDescription = goalDescription.replace(task+"\n","");
    }

    public void setCompletedDate(long input) {
        completeDate = new Date(input);
    }

    public void setReminderString(String goalReminderString) {
        this.goalReminderString = goalReminderString;
    }

    public void removeOldReminders(Date currentDate) {
        String newReminderString = "";
        List<Date> reminders = getReminderDateList();
        for(Date date : reminders) {
            if(date.after(currentDate)) {
                String dateForInsert = dateFormatForReminders.format(date);
                dateForInsert += "\n";
                newReminderString += dateForInsert;
            }
        }
        goalReminderString = newReminderString;
    }

    public void deleteReminder(String string) {
        goalReminderString = goalReminderString.replace(string,"");
    }

    public String toString() {
        return ("Goal number "+this._id+"\n"+"Title: "+ goalTitle +"\nDescription: "+ goalDescription +"\nType: "+ goalType +
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
        createSpannableString(List<String> list, boolean bullets,
                              boolean newline, boolean strikethrough) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        int current = 0;
        int location = 0;
        for(String string : list) {
            if (string.length() > 0) {
                boolean incomplete = (string.charAt(0) == '0');
                if (strikethrough) {
                    string = string.replaceFirst("[0-1]+", "");
                }
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
        }
        return builder;
    }

    /**
     * Builds a proper spannable string for a single task, with bullets but without new lines.
     * @param task
     * @return
     */
    public static SpannableStringBuilder createSpannableString(String task, boolean strikethrough) {
        List<String> singleTask = new ArrayList<String>();
        singleTask.add(task);
        return createSpannableString(singleTask, true, false, strikethrough);
    }



}

