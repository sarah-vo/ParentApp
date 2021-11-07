package ca.cmpt276.parentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

import ca.cmpt276.parentapp.model.Child;
import ca.cmpt276.parentapp.model.childManager;

public class childConfiguration extends AppCompatActivity {
    childManager manager = childManager.getInstance();
    ArrayList<Child> childrenList = manager.getChildList();
    ArrayList<String> childName = new ArrayList<>();
    int spinnerPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_configuration);

        Spinner spinner = findViewById(R.id.childrenSpinner);
        for(int i = 0; i < childrenList.size(); i++){
            childName.add(childrenList.get(i).getName());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, childName);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setSelection(0); //initial selection
        addChild(dataAdapter);
        editChild(dataAdapter);
        removeChild(dataAdapter);

    }


    private void addChild(ArrayAdapter<String> dataAdapter) {
        Button button = findViewById(R.id.addChild);
        button.setOnClickListener(View ->{
            EditText addText = findViewById(R.id.TextInputAdd);
            if(!TextUtils.isEmpty(addText.getText().toString())) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.confirm_add_child)
                        .setPositiveButton(R.string.yes_add_child, (dialog, which) -> {
                            String newName = addText.getText().toString();
                            manager.addChildren(newName);
                            childName.add(newName);
                            dataAdapter.notifyDataSetChanged();
                            String hello = "hello";
                            Log.d(hello,hello);
                        })
                        .setNegativeButton(R.string.no_add_child, (dialog, which) -> {
                        }/*do nothing*/);
                AlertDialog alert = builder.create();
                alert.show();
            }
            else{
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.empty_value)
                        .setNeutralButton(R.string.OK, (dialog, which) ->{} /*do nothing*/);
                AlertDialog alert = builder.create();
                alert.show();            }
        });



    }
    //Put AlertDialog to make sure if user wants to delete this child.
    private void removeChild(ArrayAdapter<String> dataAdapter) {
        Button button = findViewById(R.id.DeleteChild);
        button.setOnClickListener(View -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.confirm_delete_child)
                    .setPositiveButton(R.string.yes_delete_child, (dialog, which) -> {
                        manager.removeChildren(spinnerPosition);
                        childName.remove(spinnerPosition);
                        dataAdapter.notifyDataSetChanged();
                    })
                    .setNegativeButton(R.string.no_delete_child, (dialog, which) -> {
                    }/*do nothing*/);
            AlertDialog alert = builder.create();
            alert.show();
        });
    }
    private void editChild(ArrayAdapter<String> dataAdapter) {
        Button button = findViewById(R.id.EditChild);
        button.setOnClickListener(View -> {
            EditText editText = findViewById(R.id.TextInputEdit);
            if (!TextUtils.isEmpty(editText.getText().toString())) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.confirm_edit_child)
                        .setPositiveButton(R.string.yes_edit_child, (dialog, which) -> {
                            String newName = editText.getText().toString();
                            manager.editChildren(newName, spinnerPosition);
                            childName.set(spinnerPosition, newName);
                            dataAdapter.notifyDataSetChanged();
                        })
                        .setNegativeButton(R.string.no_edit_child, (dialog, which) -> {
                        }/*do nothing*/);
                AlertDialog alert = builder.create();
                alert.show();
            }
            else{
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.empty_value)
                        .setNeutralButton(R.string.OK, (dialog, which) ->{} /*do nothing*/);
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    public class SpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(this, "spinnerPosition chosen", Toast.LENGTH_LONG).show();
            spinnerPosition = position;
        }
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }


}