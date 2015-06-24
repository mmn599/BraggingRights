package io.normyle.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
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
    private static final String KEY_GOAL = "goal";

    private static final String[] COLUMNS = {KEY_ID,KEY_GOAL};

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create book table
        String CREATE_GOALS_TABLE = "CREATE TABLE "+TABLE_GOALS+"(" +
                KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_GOAL+" BLOB)";
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

    public long addGoal(Goal goal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(goal);
            byte[] bytes = bos.toByteArray();
            values.put(KEY_GOAL, bytes);
        } catch(IOException e) {

        } finally {
            try {
                if(out!=null) {
                    out.close();
                }
            } catch(IOException e) {

            }
            try {
                bos.close();
            } catch(IOException e) {

            }
        }
        // 3. insert
        long id = db.insert(TABLE_GOALS, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values
        // 4. close
        db.close();
        return id;
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

        int my_id = cursor.getInt(0);

        byte[] bytes = cursor.getBlob(1);
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInput in = null;
        Goal goal = null;
        try {
            in = new ObjectInputStream(bis);
            Object o = in.readObject();
            goal = (Goal) o;
            goal.setId(my_id);
        } catch(Exception e) {

        } finally {
            try {
                bis.close();
            } catch (IOException ex) {
                // ignore close exception
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
        }
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
        ContentValues values = new ContentValues();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(g);
            byte[] bytes = bos.toByteArray();
            values.put(KEY_GOAL, bytes);
        } catch(IOException e) {

        } finally {
            try {
                if(out!=null) {
                    out.close();
                }
            } catch(IOException e) {

            }
            try {
                bos.close();
            } catch(IOException e) {

            }
        }
        this.getWritableDatabase().update(TABLE_GOALS, values, KEY_ID+" "+"="+String.valueOf(g.getId()), null);
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
                int my_id = cursor.getInt(0);
                byte[] bytes = cursor.getBlob(1);
                ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                ObjectInput in = null;
                Goal goal = null;
                try {
                    in = new ObjectInputStream(bis);
                    Object o = in.readObject();
                    goal = (Goal) o;
                    goal.setId(my_id);
                } catch(Exception e) {

                } finally {
                    try {
                        bis.close();
                    } catch (IOException ex) {
                        // ignore close exception
                    }
                    try {
                        if (in != null) {
                            in.close();
                        }
                    } catch (IOException ex) {
                        // ignore close exception
                    }
                }
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

    //TODO: IMPORTANT: determine if this at all helps performance
    public static void updateGoalInBackground(Context context,Goal g) {
        new UpdateGoalTask().execute(new MySQLiteHelper(context),g);
    }

    public static void updateGoal(Context context, Goal g) {
        MySQLiteHelper db = new MySQLiteHelper(context);
        db.updateGoal(g);
        db.close();
    }

    public static void deleteGoal(Context context, Goal g) {
        MySQLiteHelper db = new MySQLiteHelper(context);
        db.deleteGoal(g);
        db.close();
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
