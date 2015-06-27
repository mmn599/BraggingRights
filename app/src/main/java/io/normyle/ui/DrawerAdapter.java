package io.normyle.ui;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import io.matthew.braggingrights.R;
import io.normyle.braggingrights.MainActivity;

/**
 * Created by MatthewNew on 12/17/2014.
 */
public class DrawerAdapter extends ArrayAdapter<String> {

    Context context;
    int layoutResourceId;
    String data[] = null;

    public DrawerAdapter(Context context, int layoutResourceId, String[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        Holder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new Holder();
            holder.txt = (TextView) row.findViewById(R.id.txtview_item_text);
            holder.image = (ImageView) row.findViewById(R.id.imageview_drawer_image);
            row.setTag(holder);
        }
        else
        {
            holder = (Holder)row.getTag();
        }

        String location = data[position];
        if(location.equals(MainActivity.PRESENT_TITLE)) {
            holder.image.setImageResource(R.drawable.present_icon);
        }
        else if(location.equals(MainActivity.PAST_TITLE)) {
            holder.image.setImageResource(R.drawable.past_icon);
        }
        else if(location.equals(MainActivity.PERSONHOOD_TITLE))  {
            holder.image.setImageResource(R.drawable.personhood_icon);
        }
        else {
            holder.image.setImageResource(R.drawable.gear);
        }
        holder.txt.setText(location);
        return row;
    }

    static class Holder
    {
        TextView txt;
        ImageView image;
    }

}
