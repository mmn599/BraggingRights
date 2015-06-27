package io.normyle.braggingrights;

import android.graphics.Point;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;

import io.matthew.braggingrights.R;
import io.normyle.data.Constants;
import io.normyle.ui.GoalTypeView;

public class AnimationActivity extends ActionBarActivity {

    int mNumX;
    int mNumY;

    int mScreenX;
    int mScreenY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);

        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        mScreenX = point.x;
        mScreenY = point.y;
        mNumX = mScreenX/GoalTypeView.DEFAULT_SIZE;
        mNumY = mScreenY/GoalTypeView.DEFAULT_SIZE;

    }
}
