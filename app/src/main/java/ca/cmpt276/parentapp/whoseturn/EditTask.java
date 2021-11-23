package ca.cmpt276.parentapp.whoseturn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import ca.cmpt276.parentapp.R;
import ca.cmpt276.parentapp.model.Child;
import ca.cmpt276.parentapp.model.Task;
import ca.cmpt276.parentapp.model.TaskManager;

/**
 * Screen to let user change task name, delete the task, and confirm the child has complete task.
 */
public class EditTask extends AppCompatActivity {
    TaskManager taskManager = TaskManager.getInstance();
    Task task;
    int taskIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        setTitle("Modify Task");

        Intent intent = getIntent();
        taskIndex = intent.getIntExtra("task index", -1);
        task = taskManager.getTask(taskIndex);

        setupText();
        setupButton();
        onClickDone();
    }

    public void onClickDone() {

        Button mEditTaskButton = findViewById(R.id.button_edit_task_done);
        EditText mEditTaskName = findViewById(R.id.etTaskName);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String oldTaskName = mEditTaskName.getText().toString();

        mEditTaskButton.setOnClickListener((view) -> {
            if (TextUtils.isEmpty(mEditTaskName.getText().toString())) {
                builder
                        .setMessage(R.string.no_edit_task_exit)
                        .setNeutralButton(R.string.OK, (dialog, which) -> onClickDone());

                AlertDialog alert = builder.create();
                alert.show();
            } else {
                String newTaskName;
                newTaskName = mEditTaskName.getText().toString();
                builder
                        .setMessage(getString(R.string.confirm_edit_task, newTaskName))
                        .setPositiveButton(R.string.yes_add_task, (dialog, which) -> {
                            task.setTaskName(newTaskName);
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
        EditText mEditTaskName = findViewById(R.id.etTaskName);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (TextUtils.isEmpty(mEditTaskName.getText().toString())) {
            builder.setMessage(R.string.back_edit_task_without_value)
                    .setPositiveButton (R.string.yes_add_task, (dialog, which) -> {
                        finish();
                    })
                    .setNegativeButton(R.string.no_add_task, (dialog, which) -> {
                        onClickDone();
                    });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            builder.setMessage(R.string.back_edit_task_without_done)
                    .setPositiveButton (R.string.yes_add_task, (dialog, which) -> super.onBackPressed())
                    .setNegativeButton(R.string.no_add_task, (dialog, which) -> onClickDone());
            AlertDialog alert = builder.create();
            alert.show();
        }

    }

    private void setupText() {
        EditText etTaskName = findViewById(R.id.etTaskName);
        etTaskName.setText(task.getTaskName());

        Child child = task.getCurrentTurnChild();
        TextView tvChildName = findViewById(R.id.tvChildName);
        ImageView childPhoto = findViewById(R.id.ivPhoto);
        if (child != null) {
            tvChildName.setText(child.getChildName());
            if(child.getPortrait() != null) {
                childPhoto.setImageBitmap(child.getPortrait());
            }
        }

    }

    private void setupButton() {
        Button btnComplete = findViewById(R.id.button_complete);
        Button btnCancel = findViewById(R.id.button_edit_task_done);

        btnComplete.setOnClickListener(View -> {
            task.passTurnToNextChild();
            finish();
        });
        btnCancel.setOnClickListener(View -> finish());
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_task,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_delete_task:
                taskManager.removeTask(taskIndex);

            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}