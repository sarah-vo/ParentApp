package ca.cmpt276.parentapp.whoseturn;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ca.cmpt276.parentapp.R;
import ca.cmpt276.parentapp.model.ChildManager;
import ca.cmpt276.parentapp.model.History;

/**
 *An adapter used to show the history of a task list onto a Recycler View.
 */

public class TaskHistoryAdapter extends RecyclerView.Adapter<TaskHistoryAdapter.TaskHistoryViewHolder> {

    Context context;
    ArrayList<History> taskHistoryList;
    ChildManager childManager = ChildManager.getInstance();

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

        int childIndex = taskHistoryList.get(position).getChildIndex();

        if(childIndex == -1) {
            holder.childPortrait.setImageResource(R.drawable.default_portrait);
            holder.childName.setText(R.string.task_history_deleted_child);
            holder.lastTurnDate.setText(taskHistoryList.get(position).getLastTurnDate());
        }

        else if (childManager.getChildList().size() != 0) {
            if (childManager.getChild(childIndex) != null) {
                if(childManager.getChild(childIndex).getPortrait() != null) {
                    holder.childPortrait.setImageBitmap(childManager.getChild(childIndex).getPortrait());
                }
                else {
                    holder.childPortrait.setImageResource(R.drawable.default_portrait);
                }

                if(childManager.getChild(childIndex).getChildName() != null) {
                    holder.childName.setText(childManager.getChild(childIndex).getChildName());
                }
                else {
                    holder.childName.setText(R.string.task_history_deleted_child);
                }

                if(taskHistoryList.get(position).getLastTurnDate() != null) {
                    holder.lastTurnDate.setText(taskHistoryList.get(position).getLastTurnDate());
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return taskHistoryList.size();
    }

    public static class TaskHistoryViewHolder extends RecyclerView.ViewHolder {

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
