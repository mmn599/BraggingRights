package io.normyle.data;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

import io.matthew.braggingrights.R;

/**
 * Created by MatthewNew on 12/18/2014.
 */
public class Constants {

    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;

    public static ArrayList<GoalType> getGoalTypes() {

        ArrayList<GoalType> goalTypes = new ArrayList<GoalType>();

        /* just for practice */
        goalTypes.add(new GoalType("Mind", R.drawable.mind_icon));
        goalTypes.add(new GoalType("Body", R.drawable.body_icon));
        goalTypes.add(new GoalType("Spirit", R.drawable.spirit_icon));

        return goalTypes;
    }


    public static class GoalType {

        String type;
        int image_drawable_id;

        public GoalType(String type, int image_drawable_id) {
            this.type = type;
            this.image_drawable_id = image_drawable_id;
        }

        public String getType() {
            return type;
        }

        public int getImageId() {
            return image_drawable_id;
        }

    }

}


