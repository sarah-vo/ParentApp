package ca.cmpt276.parentapp.newConfig;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import ca.cmpt276.parentapp.MainActivity;
import ca.cmpt276.parentapp.R;
import ca.cmpt276.parentapp.model.Child;
import ca.cmpt276.parentapp.model.ChildManager;

/**Function that allows user to add/edit child information.**/
public class ConfigActivity extends AppCompatActivity {
    ChildManager manager = ChildManager.getInstance();
    public static final String SHARED_PREFERENCE = "Shared Preference";
    public static final String CHILD_LIST = "Child List";
    ArrayAdapter<Child> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        buildChildList();
        buildFloatingButton();
        deleteAllDEBUGButton();
        addChildrenDEBUGButton();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    protected void onResume() {
        loadData();
        buildChildList();
        buildFloatingButton();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        saveData();
        super.onDestroy();
    }

    private void buildChildList() {
        adapter = new ListViewAdapter(this, manager.getChildList());
        ListView list = findViewById(R.id.childListView);
        list.setAdapter(adapter);
        list.setOnItemClickListener((parent, viewClicked, position, id) -> {
            Intent intent = new Intent(this, ModifyDeleteChildren.class);
            intent.putExtra("Child Position",position);
            startActivity(intent);
            saveData();
        });
    }

    private void buildFloatingButton() {
        FloatingActionButton button = findViewById(R.id.configFloatingButton);
        button.setOnClickListener(View ->{
            Intent intent = new Intent(this, AddChildren.class);
            startActivity(intent);
            saveData();
        });
    }

    //Save current data of the gameManager using SharedPreferences
    void saveData(){
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
        manager = gson.fromJson(json, ChildManager.class);
        ChildManager.setInstance(manager);

        if(manager == null){
            manager = ChildManager.getInstance();
        }

        else{
            Log.i("numChild_load", manager.getChildList().size() + "");
        }
    }

    /** FOR DEBUG PURPOSES **/ //TODO: REMOVE THIS BEFORE SUBMISSION
    private void deleteAllDEBUGButton() {
        Button button = findViewById(R.id.deleteAllChildButton);
        button.setOnClickListener(View ->{
            manager.getChildList().clear();
            adapter.notifyDataSetChanged();
            saveData();
        });
    }
    /** FOR DEBUG PURPOSES **/ //TODO: REMOVE THIS BEFORE SUBMISSION
    private void addChildrenDEBUGButton(){
        Button button = findViewById(R.id.createChildren);
        button.setOnClickListener(View ->{
            int i = (manager.getChildList().isEmpty())?(0):(manager.getChildList().size());
            manager.addChildren(Integer.toString(i), null);
            adapter.notifyDataSetChanged();
        });


    }

}