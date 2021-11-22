package ca.cmpt276.parentapp.whoseturn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ca.cmpt276.parentapp.R;
import ca.cmpt276.parentapp.model.Task;
import ca.cmpt276.parentapp.model.TaskManager;

public class EditTask extends AppCompatActivity {
    TaskManager taskManager = TaskManager.getInstance();
    Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        setTitle("Modify Task");

        Intent intent = getIntent();
        int index = intent.getIntExtra("task index", -1);
        task = taskManager.getTask(index);

        setupText();
        setupButton();
    }

    private void setupText() {
        EditText editTaskName = findViewById(R.id.etTaskName);
        editTaskName.setText(task.getTaskName());

        editTaskName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String newTask = editTaskName.getText().toString();
                task.setTaskName(newTask);
            }
        });

        TextView tvChildName = findViewById(R.id.tvChildName);
        tvChildName.setText(task.getCurrentTurnChild());
    }

    private void setupButton() {
        Button buttonComplete = findViewById(R.id.buttotn_complete);
        Button buttonCancel = findViewById(R.id.button_cancel);

        buttonComplete.setOnClickListener(View -> {
            task.passTurnToNextChild();
            finish();
        });

        buttonCancel.setOnClickListener(View -> {
            finish();
        });
    }

}