package ca.cmpt276.parentapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
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

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.ArrayList;

import ca.cmpt276.parentapp.model.Child;
import ca.cmpt276.parentapp.model.FlipCoin;
import ca.cmpt276.parentapp.model.FlipCoinManager;
import ca.cmpt276.parentapp.model.childManager;

public class ChildConfigurationActivity extends AppCompatActivity {

    public static final String SHARED_PREFERENCE = "Shared Preference";
    public static final String CHILD_LIST = "Child List";

    childManager manager;

    ArrayList<Child> childrenList;
    ArrayList<String> childName;

    ArrayAdapter<String> dataAdapter;

    int spinnerPosition;
    Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_configuration);

        manager = childManager.getInstance();
        childrenList = manager.getChildList();
        childName = new ArrayList<>();

        spinner = findViewById(R.id.childrenSpinner);
        for(int i = 0; i < childrenList.size(); i++){
            childName.add(childrenList.get(i).getName());
        }

        dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,
                childName);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);
        spinner.setSelection(0); //initial selection

        addChild(dataAdapter);
        editChild(dataAdapter);
        removeChild(dataAdapter);
        this.setTitle("Configure My Children");
    }

    @Override
    protected void onResume() {
        loadData();

        childrenList = manager.getChildList();
        childName = new ArrayList<>();

        spinner = findViewById(R.id.childrenSpinner);
        for(int i = 0; i < childrenList.size(); i++){
            childName.add(childrenList.get(i).getName());
        }

        dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                childName);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);
        spinner.setSelection(0); //initial selection

        addChild(dataAdapter);
        editChild(dataAdapter);
        removeChild(dataAdapter);

        super.onResume();
    }

    @Override
    protected void onDestroy() {
        saveData();
        super.onDestroy();
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

                            saveData();
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
                alert.show();
            }
        });

    }

    //Put AlertDialog to make sure if user wants to delete this child.
    private void removeChild(ArrayAdapter<String> dataAdapter) {
        Button button = findViewById(R.id.DeleteChild);
        button.setOnClickListener(View -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.confirm_delete_child)
                    .setPositiveButton(R.string.yes_delete_child, (dialog, which) -> {
                        manager.removeChildren(spinner.getSelectedItemPosition());
                        childName.remove(spinner.getSelectedItemPosition());
                        dataAdapter.notifyDataSetChanged();

                        saveData();
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

                            manager.editChildren(newName, spinner.getSelectedItemPosition());
                            childName.set(spinner.getSelectedItemPosition(), newName);
                            dataAdapter.notifyDataSetChanged();

                            saveData();
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

    //Save current data of the gameManager using SharedPreferences
    private void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //Convert gridManager to json format
        Gson gson = new Gson();
        String json = gson.toJson(manager);

        //Save the json
        editor.putString(CHILD_LIST,json);
        editor.apply();
    }

    //Load data from saved state
    private void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCE, MODE_PRIVATE);

        //Get the gridManager in json format
        Gson gson = new Gson();
        String json = sharedPreferences.getString(CHILD_LIST,null);

        //Covert the gameManager into an Object and set the instance to the specified gameManager
        manager = gson.fromJson(json, childManager.class);
        childManager.setInstance(manager);

        if(manager == null){
            manager = childManager.getInstance();
        }

        else{
            Log.i("numChild_load", manager.getChildList().size() + "");
        }
    }



}