package io.normyle.data;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.style.BulletSpan;
import android.text.style.StrikethroughSpan;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import io.matthew.braggingrights.R;
import io.normyle.braggingrights.Notifications;

/**
 * Constants class provides utility methods for accessing and creating Goal Categories aka Goal Types
 */
public class Constants {

    /*
     * GoalType objects define the different categories goals can be created under
     * The user defines their different goal types in the SettingsFragment within the MainActivity
     * Essentially, a goal type is a string for it's name, a color, and an icon
     */
    public static class GoalType {

        String type;
        int image_drawable_id;
        int color;

        public GoalType(String type, int image_drawable_id, int color) {
            this.type = type;
            this.color = color;
            this.image_drawable_id = image_drawable_id;
        }

        public String getType() {
            return type;
        }

        public int getImageId() {
            return image_drawable_id;
        }

        public int getColor() {
            return color;
        }

        @Override
        public boolean equals(Object o) {
            if(!(o instanceof GoalType)) {
                return false;
            }
            GoalType other = (GoalType) o;
            return this.type.equals(other.getType());
        }
    }

    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;

    /**
     * Utility method for creating a pretty looking sentence preceded with a bullet point
     * @param string String to bulletize
     * @return returns SpannableStringBuilder with info about bulleted string
     */
    public static SpannableStringBuilder createBulletString(String string) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder = builder.append(string);
        builder.setSpan(new BulletSpan(15), 0, string.length(), 0);
        return builder;
    }

    /**
     * Utility method to determine whether the app is setup (>0 goal categories)
     * @param context Context neccesary for accessing types file
     * @return
     */
    public static boolean isSetup(Context context) {
        return getGoalTypes(context).size()!=0;
    }

    public static String TYPES_FILE_NAME_STRING = "typesFiles";

    /**
     * @param context neccesary for accessing goal types file
     * @return Returns a list of the GoalType objects the user has defined
     */
    public static List<GoalType> getGoalTypes(Context context) {
        List<GoalType> ret = new ArrayList<>();
        File file = new File(context.getFilesDir(), TYPES_FILE_NAME_STRING);
        if(file.exists()) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(file));
                int num = Integer.parseInt(reader.readLine());
                for(int i=0;i<num;i++) {
                    String type = reader.readLine();
                    int resource = Integer.parseInt(reader.readLine());
                    int color = Integer.parseInt(reader.readLine());
                    GoalType goalType = new GoalType(type,resource,color);
                    ret.add(goalType);
                }
                return ret;
            } catch(IOException e) {
                Toast.makeText(context, "Error reading settings", Toast.LENGTH_LONG).show();
            } finally {
                if(reader!=null) {
                    try {
                        reader.close();
                    } catch(IOException e) {
                        Toast.makeText(context, "Error reading settings", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
        return ret;
    }

    /**
     * Updates types defined in Goal Type file
     * @param context
     * @param types list of new types defined
     */
    public static void writeTypes(Context context,List<GoalType> types) {
        File file = new File(context.getFilesDir(),TYPES_FILE_NAME_STRING);
        PrintWriter wr = null;
        try {
            wr = new PrintWriter(new FileWriter(file));
            wr.println(types.size());
            for(GoalType gt : types) {
                wr.println(gt.type);
                wr.println(gt.image_drawable_id+"");
                wr.println(gt.color+"");
            }
        } catch(IOException e) {

        } finally {
            wr.close();
        }
    }

    /**
     * Utility method to add a defined goal type
     * @param context
     * @param type
     */
    public static void addGoalType(Context context, GoalType type) {
        List<GoalType> existingTypes = Constants.getGoalTypes(context);
        existingTypes.add(type);
        writeTypes(context,existingTypes);
    }

    /**
     * Removed a goal type and it's goals from the database
     * @param context
     * @param type
     */
    public static void deleteGoalType(Context context, GoalType type) {
        MySQLiteHelper db = new MySQLiteHelper(context);
        List<Goal> goals = db.getAllGoals();
        List<Goal> relevantGoals = Goal.getGoalsOfType(goals, type);
        for(Goal goal : relevantGoals) {
            db.deleteGoal(goal);
        }
        db.close();
        List<GoalType> existingTypes = Constants.getGoalTypes(context);
        existingTypes.remove(type);
        writeTypes(context,existingTypes);
    }

    public static final String TAG = "BRAGTAG";

}


