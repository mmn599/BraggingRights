package io.normyle.braggingrights;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import io.matthew.braggingrights.R;
import io.normyle.data.Constants;
import io.normyle.ui.GoalTypeViewer;
import io.normyle.ui.MagicListener;
import it.sephiroth.android.library.widget.HListView;

public class SettingsFragment extends Fragment implements View.OnClickListener, MagicListener {

    Button mBtnAdd;
    LinearLayout mLLTypes;

    EditText mTxtGoalTypeName;
    ColorPicker mColorPicker;
    FloatingActionButton mFab;
    HListView mListViewIcons;
    TextView mTxtIntro;
    HorizontalScrollView mScrollGoalTypes;

    GoalTypeViewer mViewerGoalType;

    boolean mSetup;

    IconAdapter mIconAdapter;

    MainActivity mActivity;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (MainActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_settings, container, false);
        mBtnAdd = (Button) v.findViewById(R.id.btn_add);
        mBtnAdd.setOnClickListener(this);
        mLLTypes = (LinearLayout) v.findViewById(R.id.ll_types);
        mTxtGoalTypeName = (EditText) v.findViewById(R.id.txt_goal_type_name);
        mColorPicker = (ColorPicker) v.findViewById(R.id.colorpicker);
        mTxtGoalTypeName.setVisibility(View.GONE);
        mColorPicker.setVisibility(View.GONE);
        mColorPicker.setShowOldCenterColor(false);
        mFab = (FloatingActionButton) v.findViewById(R.id.btn_complete_button);
        mFab.setVisibility(View.GONE);
        mFab.setOnClickListener(this);
        mListViewIcons = (HListView) v.findViewById(R.id.hl_icons);
        TypedArray imgs = getResources().obtainTypedArray(R.array.icon_arr);
        List<Integer> data = new ArrayList<>();
        for(int i=0;i<imgs.length();i++) {
            data.add(Integer.valueOf(imgs.getResourceId(i, -1)));
        }
        mIconAdapter = new IconAdapter(
                getActivity(),R.layout.icon_row,data);
        mListViewIcons.setAdapter(mIconAdapter);
        mListViewIcons.setVisibility(View.GONE);

        mScrollGoalTypes = (HorizontalScrollView) v.findViewById(R.id.scroll_goal_types);
        mViewerGoalType = (GoalTypeViewer) v.findViewById(R.id.viewer_goal_types);
        mViewerGoalType.setListener(this);
        mViewerGoalType.setReclicks(false);
        mViewerGoalType.clearSelected();
        mTxtIntro = (TextView) v.findViewById(R.id.txt_intro);
        if(!mActivity.isSetup()) {
            //not setup yet
            mSetup = false;
            introSetup();
        }
        else {
            //already setup
            nonIntroSetup();
            mSetup = true;
            mTxtIntro.setVisibility(View.GONE);
        }

        mTxtGoalTypeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTxtIntro.setVisibility(View.GONE);
                mFab.setVisibility(View.VISIBLE);
            }
        });

        return v;
    }

    private void introSetup() {
        mTxtIntro.setTextSize(14);
        mTxtIntro.setText("Welcome to Bragging Rights!\nTo get started, add one or more goal categories using the interface below." +
                " These categories are the different areas of your life you want to complete goals in. Once you've added some, explore the app and start" +
                " completing goals!");
        mViewerGoalType.setVisibility(View.INVISIBLE);
        mBtnAdd.setVisibility(View.GONE);
        mTxtGoalTypeName.setVisibility(View.VISIBLE);
        mColorPicker.setVisibility(View.VISIBLE);
        mListViewIcons.setVisibility(View.VISIBLE);
//       LinearLayout.LayoutParams params =
//                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        params.setMargins(0,0,0,0);
//        mScrollGoalTypes.setLayoutParams(params);
        mScrollGoalTypes.setVisibility(View.INVISIBLE);
    }

    private void nonIntroSetup() {
    }

    private static class IconAdapter extends ArrayAdapter<Integer> implements View.OnClickListener {

        private Context mContext;
        private List<Integer> mData;
        private int mResourceId;
        private View mSelected;

        public IconAdapter(Context context, int layout_resource_id, List<Integer> data) {
            super(context,layout_resource_id,data);
            mData = data;
            mResourceId = layout_resource_id;
            mContext = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null) {
                LayoutInflater inflater =
                        (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(mResourceId,parent,false);
            }
            ImageView imgView = (ImageView) convertView;
            imgView.setImageResource(mData.get(position));
            imgView.setTag(mData.get(position));
            imgView.setOnClickListener(this);
            return imgView;
        }

        @Override
        public void onClick(View v) {
            if(mSelected!=null) {
                mSelected.setSelected(false);
                mSelected.setBackgroundColor(Color.TRANSPARENT);
            }
            mSelected = v;
            v.setBackgroundColor(mContext.getResources().getColor(R.color.amber_500));
            mSelected.setSelected(true);
        }

        public int getImgId() {
            if(mSelected!=null) {
                return (Integer) mSelected.getTag();
            }
            return 0;
        }
    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_add) {
            if(mSetup && mTxtIntro.getVisibility()==View.VISIBLE) {
                mTxtIntro.setVisibility(View.GONE);
            }
            if(mViewerGoalType.getSelected().equals(MagicListener.UNSELECTED_STRING)) {
                v.setVisibility(View.GONE);
                mTxtGoalTypeName.setVisibility(View.VISIBLE);
                mColorPicker.setVisibility(View.VISIBLE);
                mFab.setVisibility(View.VISIBLE);
                mListViewIcons.setVisibility(View.VISIBLE);
            }
            else {
                //trying to delete a type
                deleteType(mViewerGoalType.getSelected());
                mViewerGoalType.clearSelected();
                mBtnAdd.setText("Add");
            }
        }
        if(v.getId()==R.id.btn_complete_button) {
            if(validateInput()) {
                if(!mSetup) {
                    mSetup = true;
                    mScrollGoalTypes.setVisibility(View.VISIBLE);
                    mViewerGoalType.setVisibility(View.VISIBLE);
                    mActivity.finishedSetup();
                }
                else {
                    mTxtIntro.setVisibility(View.GONE);
                }
                v.setVisibility(View.GONE);
                Constants.GoalType goalType = new Constants.GoalType(mTxtGoalTypeName.getText().toString(),
                        mIconAdapter.getImgId(),mColorPicker.getColor());
                Constants.addGoalType(mActivity, goalType);
                mViewerGoalType.updateData();
                mTxtGoalTypeName.setVisibility(View.GONE);
                mColorPicker.setVisibility(View.GONE);
                mFab.setVisibility(View.GONE);
                mListViewIcons.setVisibility(View.GONE);
                mTxtGoalTypeName.setText("");
                mBtnAdd.setVisibility(View.VISIBLE);
                mFab.setVisibility(View.GONE);
            }
        }
    }

    private void deleteType(String type) {
        Constants.GoalType goalType = null;
        for(Constants.GoalType gt : Constants.getGoalTypes(mActivity)) {
            if(gt.getType().equals(type)) {
                goalType = gt;
            }
        }
        if(goalType!=null) {
            Constants.deleteGoalType(mActivity, goalType);
            mViewerGoalType.updateData();
        }
    }

    @Override
    public void onChange(String type) {
        if(type.equals(UNSELECTED_STRING)) {
            mBtnAdd.setText("Add");
        }
        else {
            mBtnAdd.setText("Delete");
        }
    }

    private boolean validateInput() {
        List<Constants.GoalType> goalTypes = Constants.getGoalTypes(mActivity);
        if(mTxtGoalTypeName.getText().toString().length()==0) {
            Toast.makeText(mActivity,"Remember to put a name for your category",Toast.LENGTH_LONG).show();
            return false;
        }
        String name = mTxtGoalTypeName.getText().toString();
        for(Constants.GoalType type : goalTypes) {
            if(name.equals(type.getType())) {
                Toast.makeText(mActivity,"Don't use a category name more than once",
                        Toast.LENGTH_LONG).show();
                return false;
            }
        }
        if(mIconAdapter.getImgId()==0) {
            Toast.makeText(mActivity,"Remember to choose a category icon",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
