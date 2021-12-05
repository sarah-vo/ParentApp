package ca.cmpt276.parentapp.whoseturn;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;


import ca.cmpt276.parentapp.R;
import ca.cmpt276.parentapp.model.Task;
import ca.cmpt276.parentapp.model.TaskManager;

/**
 * Screen to let user add task.
 */

public class AddTaskActivity extends AppCompatActivity {

    TaskManager taskManager;
    String taskName;
    Task newTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        setTitle("Add Task");

        taskManager = TaskManager.getInstance();
        newTask = new Task(taskName);

        onClickDone();

    }

    public void onClickDone() {
        Button mAddTaskButton = findViewById(R.id.button_done);
        EditText mTaskNameEntry = findViewById(R.id.add_task_name);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        mAddTaskButton.setOnClickListener((view) -> {
            if (TextUtils.isEmpty(mTaskNameEntry.getText().toString())) {
                builder
                        .setMessage(R.string.no_new_task_exit)
                        .setPositiveButton(R.string.yes_add_task, (dialog, which) -> finish())
                        .setNegativeButton(R.string.no_add_task, (dialog, which) -> onClickDone());

                AlertDialog alert = builder.create();
                alert.show();
            } else {
                taskName = mTaskNameEntry.getText().toString();
                builder
                        .setMessage(getString(R.string.confirm_add_task, taskName))
                        .setPositiveButton(R.string.yes_add_task, (dialog, which) -> {
                            newTask.setTaskName(taskName);
                            taskManager.addTask(newTask);
                            finish();
                        })
                        .setNegativeButton(R.string.no_add_task, (dialog, which) -> onClickDone());

                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        //confirm if user wanted to exit
        EditText mTaskNameEntry = findViewById(R.id.add_task_name);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if (TextUtils.isEmpty(mTaskNameEntry.getText().toString())) {
            builder.setMessage(R.string.back_without_value)
                    .setPositiveButton (R.string.yes_add_task, (dialog, which) -> super.onBackPressed())
                    .setNegativeButton(R.string.no_add_task, (dialog, which) -> onClickDone());

            AlertDialog alert = builder.create();
            alert.show();
        } else {
            builder.setMessage(R.string.back_without_done)
                    .setPositiveButton (R.string.yes_add_task, (dialog, which) -> super.onBackPressed())
                    .setNegativeButton(R.string.no_add_task, (dialog, which) -> onClickDone());

            AlertDialog alert = builder.create();
            alert.show();
        }

    }
}
