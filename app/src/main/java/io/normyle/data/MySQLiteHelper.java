package io.normyle.data;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "GoalsDB";
    private static final String TABLE_GOALS = "goals";

    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "Title";
    private static final String KEY_DESCRIPTION = "Description";
    private static final String KEY_TYPE = "Type";
    private static final String KEY_TIME = "Time";
    private static final String KEY_IMPORTANCE = "Importance";
    private static final String KEY_COMPLETE = "Complete";
    private static final String KEY_NOTES = "Notes";
    private static final String KEY_DATE = "Date";
    private static final String KEY_COMPLETE_DATE = "EndDate";

    private static final String[] COLUMNS = {KEY_ID,KEY_TITLE,KEY_DESCRIPTION,KEY_TYPE,KEY_TIME,KEY_IMPORTANCE,KEY_COMPLETE,KEY_NOTES,KEY_DATE,KEY_COMPLETE_DATE};

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create book table
        String CREATE_GOALS_TABLE = "CREATE TABLE "+TABLE_GOALS+"(" +
                KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_TITLE+" TEXT,"+
                KEY_DESCRIPTION+" TEXT,"+
                KEY_TYPE+" TEXT,"+
                KEY_TIME+" TEXT,"+
                KEY_IMPORTANCE+" INTEGER,"+
                KEY_COMPLETE+" INTEGER,"+
                KEY_NOTES+" TEXT,"+
                KEY_DATE+" INTEGER,"+
                KEY_COMPLETE_DATE+" INTEGER"+")";
        // create goals table
        db.execSQL(CREATE_GOALS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older goals table if existed
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_GOALS);
        // create fresh books table
        this.onCreate(db);
    }

    public void addGoal(Goal goal) {
        //for logging
        Log.d("addGoals", goal.toString());
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, goal.getTitle()); // get title
        values.put(KEY_DESCRIPTION, goal.getDescription()); // get author
        values.put(KEY_TYPE, goal.getType());
        values.put(KEY_TIME, goal.getTime());
        values.put(KEY_IMPORTANCE, goal.getImportance());
        values.put(KEY_COMPLETE, goal.getComplete());
        values.put(KEY_NOTES, goal.getGoalNotes());
        values.put(KEY_DATE, goal.getStartDateLong());
        values.put(KEY_COMPLETE_DATE, goal.getCompletedDateLong());
        // 3. insert
        db.insert(TABLE_GOALS, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values
        // 4. close
        db.close();
    }

    public Goal getGoal(int id){

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_GOALS, // a. table
                        COLUMNS, // b. column names
                        KEY_ID + "=?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        // 4. build book object
        Goal goal = new Goal();
        goal.setId(Integer.parseInt(cursor.getString(0)));
        goal.setTitle(cursor.getString(1));
        goal.setDescription(cursor.getString(2));
        goal.setType(cursor.getString(3));
        goal.setTime(cursor.getString(4));
        goal.setImportance(Integer.parseInt(cursor.getString(5)));
        goal.setComplete(Integer.parseInt(cursor.getString(6)));
        goal.setGoalNotes(cursor.getString(7));
        goal.setStartDate(Long.parseLong(cursor.getString(8)));
        goal.setCompletedDate(Long.parseLong(cursor.getString(9)));
        //log
        Log.d("getBook("+id+")", goal.toString());

        // 5. return book
        return goal;
    }

    public int getGoalsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_GOALS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        // return count
        return cursor.getCount();
    }

    public void updateGoal(Goal g) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_TITLE,g.getTitle()); //These Fields should be your String values of actual column names
        cv.put(KEY_DESCRIPTION,g.getDescription());
        cv.put(KEY_TYPE,g.getType());
        cv.put(KEY_TIME, g.getTime());
        cv.put(KEY_IMPORTANCE, g.getImportance());
        cv.put(KEY_COMPLETE, g.getComplete());
        cv.put(KEY_NOTES, g.getGoalNotes());
        cv.put(KEY_DATE, g.getStartDateLong());
        cv.put(KEY_COMPLETE_DATE, g.getCompletedDateLong());
        this.getWritableDatabase().update(TABLE_GOALS, cv, KEY_ID+" "+"="+String.valueOf(g.getId()), null);
    }

    // Getting All Contacts
    public List<Goal> getAllGoals() {
        List<Goal> goalList = new ArrayList<Goal>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_GOALS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Goal goal = new Goal();
                goal.setId(Integer.parseInt(cursor.getString(0)));
                goal.setTitle(cursor.getString(1));
                goal.setDescription(cursor.getString(2));
                goal.setType(cursor.getString(3));
                goal.setTime(cursor.getString(4));
                goal.setImportance(Integer.parseInt(cursor.getString(5)));
                goal.setComplete(Integer.parseInt(cursor.getString(6)));
                goal.setGoalNotes(cursor.getString(7));
                goal.setStartDate(Long.parseLong(cursor.getString(8)));
                goal.setCompletedDate(Long.parseLong(cursor.getString(9)));
                // Adding contact to list
                goalList.add(goal);
            } while (cursor.moveToNext());
        }
        // return contact list
        return goalList;
    }

    public void deleteGoal(Goal goal) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_GOALS, KEY_ID + " = ?",
                new String[] { String.valueOf(goal.getId()) });
        db.close();
    }

    public void deleteGoal(int goal_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_GOALS, KEY_ID + " = ?",
                new String[] { String.valueOf(goal_id) });
        db.close();
    }

    public static void updateGoalInBackground(Context context,Goal g) {
        new UpdateGoalTask().execute(new MySQLiteHelper(context),g);
    }

    private static class UpdateGoalTask extends AsyncTask<Object, Integer, Integer> {
        protected Integer doInBackground(Object... objects) {
            MySQLiteHelper db = (MySQLiteHelper) objects[0];
            Goal goal = (Goal) objects[1];
            db.updateGoal(goal);
            return 0;
        }
    }

}
