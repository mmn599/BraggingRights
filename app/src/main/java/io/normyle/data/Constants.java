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
 * Created by MatthewNew on 12/18/2014.
 */
public class Constants {

    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;

    public static SpannableStringBuilder
    createBulletString(String string) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        int current = 0;
        builder = builder.append(string);
        builder.setSpan(new BulletSpan(15), current, current + string.length(), 0);
        return builder;
    }

    public static class CustomColor {
        public String colorName;
        public int color;
        public CustomColor(String name, int color) {
            this.colorName = name;
            this.color = color;
        }
    }

    public static List<CustomColor> colors = new ArrayList<CustomColor>() {{
        add(new CustomColor("Red",R.color.red_500));
        add(new CustomColor("Amber",R.color.amber_500));
        add(new CustomColor("Bluze Grey",R.color.blue_grey_500));
        add(new CustomColor("Brown",R.color.brown_500));
        add(new CustomColor("Cyan",R.color.cyan_500));
        add(new CustomColor("Green",R.color.green_500));
        add(new CustomColor("Indigo",R.color.indigo_500));
        add(new CustomColor("Lime",R.color.lime_500));
        add(new CustomColor("Orange",R.color.orange_500));
        add(new CustomColor("Pink",R.color.pink_500));
        add(new CustomColor("Purple",R.color.purple_500));

    }};

    public static List<Integer> getAllRemainingColors(Context context) {
        List<Integer> colors = new ArrayList<>();
        Resources res = context.getResources();
        colors.add(res.getColor(R.color.amber_500));
        colors.add(res.getColor(R.color.blue_grey_500));
        colors.add(res.getColor(R.color.brown_500));
        colors.add(res.getColor(R.color.cyan_500));
        colors.add(res.getColor(R.color.green_500));
        colors.add(res.getColor(R.color.indigo_500));
        colors.add(res.getColor(R.color.lime_500));
        colors.add(res.getColor(R.color.orange_500));
        colors.add(res.getColor(R.color.pink_500));
        colors.add(res.getColor(R.color.purple_500));
        colors.add(res.getColor(R.color.teal_500));
        return colors;
    }

    public static void setup(Context context) {

    }

    public static String TYPES_FILE_NAME_STRING = "typesFiles";

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

            } finally {
                if(reader!=null) {
                    try {
                        reader.close();
                    } catch(IOException e) {

                    }
                }
            }
        }
        else {
            ret = defaultTypes(context);
        }
        return ret;
    }

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

    private static List<GoalType> defaultTypes(Context context) {
        List<GoalType> goalTypes = new ArrayList<>();
        Resources res = context.getResources();
        goalTypes.add(
                new GoalType("Mind", R.drawable.books8, res.getColor(R.color.teal_300)));
        goalTypes.add(
                new GoalType("Body", R.drawable.sprint, res.getColor(R.color.red_400)));
        goalTypes.add(
                new GoalType("Spirit", R.drawable.sunny35, res.getColor(R.color.yellow_500)));
        writeTypes(context, goalTypes);
        return goalTypes;
    }


    public static void addGoalType(Context context, GoalType type) {
        List<GoalType> existingTypes = Constants.getGoalTypes(context);
        existingTypes.add(type);
        writeTypes(context,existingTypes);
    }

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




}


