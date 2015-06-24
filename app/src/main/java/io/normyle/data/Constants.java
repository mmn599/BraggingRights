package io.normyle.data;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

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

    public static HashMap<String,GoalType> goalTypes;


    public static HashMap<String,GoalType> getGoalTypes() {

        return goalTypes;

    }

    public static void setup(Context context) {

        Resources res = context.getResources();

        goalTypes = new HashMap<String,GoalType>();

        goalTypes.put("Mind",
                new GoalType("Mind", R.drawable.books8, res.getColor(R.color.teal_300)));
        goalTypes.put("Body",
                new GoalType("Body", R.drawable.sprint, res.getColor(R.color.red_400)));
        goalTypes.put("Spirit",
                new GoalType("Spirit", R.drawable.sunny35, res.getColor(R.color.yellow_500)));

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
    }




}


