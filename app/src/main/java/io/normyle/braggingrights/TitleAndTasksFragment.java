package io.normyle.braggingrights;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.matthew.braggingrights.R;
import io.normyle.data.Goal;
import io.normyle.data.MySQLiteHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class TitleAndTasksFragment extends Fragment {

    Goal goal;
    Activity activity;
    TextView txtviewTitle;


    public TitleAndTasksFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_title_and_tasks, container, false);

        MySQLiteHelper db = new MySQLiteHelper(activity);
        goal = db.getGoal(getArguments().getInt("GOAL_ID",0));
        db.close();

        txtviewTitle = (TextView) v.findViewById(R.id.txtview_goal_view_title);
        txtviewTitle.setText(goal.getTitle());

        return v;
    }




}
