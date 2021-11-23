package ca.cmpt276.parentapp.whoseturn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
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
import android.widget.Toolbar;

import ca.cmpt276.parentapp.R;
import ca.cmpt276.parentapp.model.Child;
import ca.cmpt276.parentapp.model.Task;
import ca.cmpt276.parentapp.model.TaskManager;

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

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        setupText();
        setupButton();
    }

    private void setupText() {
        EditText etTaskName = findViewById(R.id.etTaskName);
        etTaskName.setText(task.getTaskName());

        etTaskName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String newTask = etTaskName.getText().toString();
                task.setTaskName(newTask);
            }
        });

        Child child = task.getCurrentTurnChild();
        TextView tvChildName = findViewById(R.id.tvChildName);
        ImageView childPhoto = findViewById(R.id.ivPhoto);
        if (child != null) {
            tvChildName.setText(child.getChildName());
            childPhoto.setImageBitmap(child.getPortrait());
        }

    }

    private void setupButton() {
        Button btnComplete = findViewById(R.id.buttotn_complete);
        Button btnCancel = findViewById(R.id.button_cancel);

        btnComplete.setOnClickListener(View -> {
            task.passTurnToNextChild();
            finish();
        });
        btnCancel.setOnClickListener(View -> {
            finish();
        });
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