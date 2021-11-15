package ca.cmpt276.parentapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import ca.cmpt276.parentapp.R;
import ca.cmpt276.parentapp.model.Child;

public class childAdapter extends ArrayAdapter<Child> {
    public childAdapter(Context context, ArrayList<Child> childList){
        super(context, R.layout.activity_flip_coin_queue, childList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Child child = getItem(position);

        //Make sure VIew is available
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_child_data,parent,false);
        }

        TextView name_text = convertView.findViewById(R.id.child_name_data);
        name_text.setText(child.getName());

        return convertView;
    }


}