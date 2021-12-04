package ca.cmpt276.parentapp.whoseturn;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ca.cmpt276.parentapp.R;
import ca.cmpt276.parentapp.model.History;

public class TaskHistoryAdapter extends RecyclerView.Adapter<TaskHistoryAdapter.TaskHistoryViewHolder> {

    Context context;
    ArrayList<History> taskHistoryList;

    public TaskHistoryAdapter(Context context, ArrayList<History> taskHistoryList) {
        this.context = context;
        this.taskHistoryList = taskHistoryList;
    }

    @NonNull
    @Override
    public TaskHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.task_history_list_item, parent, false);
        return new TaskHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHistoryViewHolder holder, int position) {

        if (taskHistoryList.get(position).getChild() != null) {
            if(taskHistoryList.get(position).getChild().getPortrait() != null) {
                holder.childPortrait.setImageBitmap(taskHistoryList.get(position).getChild().getPortrait());
            }
            else {
                holder.childPortrait.setImageResource(R.drawable.default_portrait);
            }

            if(taskHistoryList.get(position).getChild().getChildName() != null) {
                holder.childName.setText(taskHistoryList.get(position).getChild().getChildName());
            }

            if(taskHistoryList.get(position).getLastTurnDate() != null) {
                holder.childName.setText(taskHistoryList.get(position).getLastTurnDate());
            }
        }

        else {
            holder.childPortrait.setImageResource(R.drawable.default_portrait);
        }
    }

    @Override
    public int getItemCount() {
        return taskHistoryList.size();
    }

    public class TaskHistoryViewHolder extends RecyclerView.ViewHolder {

        ImageView childPortrait;
        TextView childName;
        TextView lastTurnDate;

        public TaskHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            childPortrait = itemView.findViewById(R.id.task_history_child_portrait);
            childName = itemView.findViewById(R.id.task_history_child_name);
            lastTurnDate = itemView.findViewById(R.id.task_history_last_turn_date);
        }

    }
}
