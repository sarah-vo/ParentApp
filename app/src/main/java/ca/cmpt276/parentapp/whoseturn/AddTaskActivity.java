package ca.cmpt276.parentapp.whoseturn;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;


import ca.cmpt276.parentapp.R;
import ca.cmpt276.parentapp.model.Task;
import ca.cmpt276.parentapp.model.TaskManager;

public class AddTaskActivity extends AppCompatActivity {

    TaskManager taskManager = TaskManager.getInstance();
    String taskName;
    Task newTask = new Task(taskName);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        setTitle("Add Task");

        AddTaskName();
        onClickDone();

    }

    public void AddTaskName() {
        EditText mTaskNameEntry = findViewById(R.id.add_task_name);
        mTaskNameEntry.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (TextUtils.isEmpty(mTaskNameEntry.getText().toString())) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder
                            .setMessage(R.string.empty_value_task)
                            .setNeutralButton(R.string.OK, (dialog, which) -> {
                                AddTaskName();
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else {
                    taskName = mTaskNameEntry.getText().toString();
                }
            }
            return false;
        });
    }

    public void onClickDone() {
        Button mAddTaskButton = findViewById(R.id.button_done);
        mAddTaskButton.setOnClickListener((view) -> {
            if (TextUtils.isEmpty(taskName)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder
                        .setMessage(R.string.no_new_task_exit)
                        .setPositiveButton(R.string.yes_add_task, (dialog, which) -> {
                            finish();
                        })
                        .setNegativeButton(R.string.no_add_task, (dialog, which) -> {
                            AddTaskName();
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
            else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder
                        .setMessage(getString(R.string.confirm_add_task, taskName))
                        .setPositiveButton(R.string.yes_add_task, (dialog, which) -> {
                            newTask.setTaskName(taskName);
                            taskManager.addTask(newTask);
                            finish();
                        })
                        .setNegativeButton(R.string.no_add_task, (dialog, which) -> {
                            AddTaskName();
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

}