package io.normyle.data;

import android.text.SpannableStringBuilder;
import android.text.style.BulletSpan;
import android.text.style.StrikethroughSpan;

import java.util.ArrayList;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class Goal {

	/*
	 * TODO
	 * implements notes functionality
	 * MAKE SERIALIZABLE
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
    private String notes;

    public static final int INCOMPLETE = 0;
    public static final int COMPLETE = 1;

    public Goal() {

    }

    public Goal(String goaltitle, String goaldescription, String goaltype, String goaltime, int goalimportance) {
        this.goalTitle = goaltitle;
        this.goalDescription = goaldescription;
        this.goalType = goaltype;
        this.goalTime = goaltime;
        this.goal_importance = goalimportance;
        _id = 0;
        complete = 0;
        notes = "";
        startDate = new Date(); //sets startDate to current time
        completeDate = new Date();
    }

    /*
     * Returns database id of goal.
     */
    public int getId() {
        return _id;
    }

    /*
     * Returns title of goal.
     */
    public String getTitle() {
        return goalTitle;
    }

    /*
     * Returns integer of goal's completeness level. For now, 0 is not complete and 1 is complete.
     */
    public int getComplete() {
        return complete;
    }

    /*
     * Returns category of goal.
     */
    public String getType() {
        return goalType;
    }

    /*
     * Returns string version of the time of goal.
     */
    public String getTime() {
        return goalTime;
    }

    /*
     * Returns start date as a pretty String.
     */
    public String getStartDateString() {
        return DateFormat.getDateInstance().format(startDate);
    }

    public long getStartDateLong() {
        return startDate.getTime();
    }

    public String getCompletedDateString() {
        return DateFormat.getDateInstance().format(completeDate);
    }

    public long getCompletedDateLong() {
        return completeDate.getTime();
    }

    /*
     * Returns the title of the goal with spaces replaced with new lines.
     */
    public String getLineTitle() {
        String title= goalTitle.replaceAll(" ","\n");
        return title;
    }

    public List<String> getTaskList() {
        String[] list = goalDescription.split("\n");
        List<String> taskList = new ArrayList<String>();
        for(int i=0;i<list.length;i++) {
            taskList.add(list[i]);
        }
        return taskList;
    }

    public String getDescription() {
        return goalDescription;
    }

    public String getNotes() {
        return notes;
    }

    public int getImportance() {
        return goal_importance;
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

    public void setImportance(int imp) {
        goal_importance = imp;
    }

    public void setNotes(String input) {
        notes = input;
    }

    public void setStartDate(long input) {
        startDate = new Date(input);
    }

    public String updateTask(String task, int complete) {
        goalDescription = goalDescription.replace(task,complete+task);
        return goalDescription;
    }

    public void setCompletedDate(long input) {
        completeDate = new Date(input);
    }

    public String toString() {
        return ("Goal number "+this._id+"\n"+"Title: "+ goalTitle +"\nDescription: "+ goalDescription +"\nType: "+ goalType +
                "\nTime: "+ goalTime +"\nImportance: "+ goal_importance +"\n"+"Complete: "+complete);
    }

    /**
     * List of strings that compose the tasks for the goal
     * @param list
     * Boolean that indicates the presence of bullets
     * @param bullets
     * Newline indicates whether or not to put newline characters
     * @param newline
     * Returns a good 'ol SpannableStringBuilder properly formatted
     * @return
     */
    public static SpannableStringBuilder createTasksSpannableString(List<String> list, boolean bullets, boolean newline) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        int current = 0;
        int end = 0;
        for(String string : list) {
            boolean incomplete = (string.charAt(0)=='0');
            string = string.replaceFirst("[0-1]+","");
            builder = builder.append(string);
            if(bullets) {
                builder.setSpan(new BulletSpan(15), current, current + string.length(), 0);
            }
            if(incomplete) {

            }
            else {
                builder.setSpan(new StrikethroughSpan(), current, current+string.length(), 0);
            }
            if(newline) {
                builder.append("\n");
            }
            current += newline ? string.length() + 1 : string.length();
        }
        return builder;
    }

    public static SpannableStringBuilder createTasksSpannableString(String task) {
        List<String> singleTask = new ArrayList<String>();
        singleTask.add(task);
        return createTasksSpannableString(singleTask, true, false);
    }

}

