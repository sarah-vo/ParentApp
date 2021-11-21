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
        EditText etTaskName = (EditText) findViewById(R.id.etTaskName);
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

        TextView tvChildName = (TextView) findViewById(R.id.tvChildName);
        tvChildName.setText(task.getCurrentTurnChild());
    }

    private void setupButton() {
        Button btnComplete = (Button) findViewById(R.id.btnComplete);
        Button btnCancel = (Button) findViewById(R.id.btnCancel);

        btnComplete.setOnClickListener(View -> {
            task.passTurnToNextChild();
            finish();
        });
        btnCancel.setOnClickListener(View -> {
            finish();
        });
    }

}