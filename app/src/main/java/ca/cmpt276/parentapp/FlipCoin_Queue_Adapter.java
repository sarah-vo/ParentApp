package ca.cmpt276.parentapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import ca.cmpt276.parentapp.model.Child;

public class FlipCoin_Queue_Adapter extends ArrayAdapter<Child> {
    public FlipCoin_Queue_Adapter(Context context, ArrayList<Child> childList){
        super(context, R.layout.activity_flip_coin_queue, childList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Child child = getItem(position);

        //Make sure VIew is available
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_queue_data,parent,false);
        }

        TextView name_text = convertView.findViewById(R.id.child_name_data);
        name_text.setText(getContext().getString(R.string.format_name, child.getName()));

        ImageView player_profile = convertView.findViewById(R.id.flipCoin_queue_profile);
        //TODO: SET CHILD PROFILE PHOTO HERE

        return convertView;
    }


}