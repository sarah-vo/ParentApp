package ca.cmpt276.parentapp.child_config;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ca.cmpt276.parentapp.R;
import ca.cmpt276.parentapp.model.Child;

/**Function that generates a list view for configActivity based on information gathered from childManager**/
public class ListViewAdapter extends ArrayAdapter<Child> {
    public ListViewAdapter(Context context , ArrayList<Child> childList) {
        super(context, R.layout.activity_config, childList);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Child child = getItem(position);

        View itemView = convertView;
        if(itemView == null){
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.config_listview, parent, false);
        }

        if (child != null){
            //image
            ImageView imageView = itemView.findViewById(R.id.portrait);
                imageView.setImageBitmap(child.getPortrait());


            //name
            TextView name = itemView.findViewById(R.id.name);
            name.setText("Name: " + child.getChildName());
        }

        return itemView;
    }
}
