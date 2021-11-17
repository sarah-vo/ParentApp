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

import ca.cmpt276.parentapp.R;
import ca.cmpt276.parentapp.model.Child;
import ca.cmpt276.parentapp.model.childManager;

public class configActivity extends AppCompatActivity {
    childManager manager = childManager.getInstance();
    public static final String SHARED_PREFERENCE = "Shared Preference";
    public static final String CHILD_LIST = "Child List";
    ArrayAdapter<Child> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        buildChildList();
        buildFloatingButton();
        deleteAllButton();
    }

    /** FOR DEBUG PURPOSES **/
    private void deleteAllButton() {
        Button button = findViewById(R.id.deleteAllChildButton);
        button.setOnClickListener(View ->{
            manager.getChildList().clear();
            adapter.notifyDataSetChanged();
            saveData();
        });
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
        adapter = new listViewAdapter(this, manager.getChildList());
        ListView list = findViewById(R.id.childListView);
        list.setAdapter(adapter);
        list.setOnItemClickListener((parent, viewClicked, position, id) -> {
            Intent intent = new Intent(this, modifyDeleteChildren.class);
            intent.putExtra("Child Position",position);
            startActivity(intent);
            saveData();
        });
    }

    private void buildFloatingButton() {
        FloatingActionButton button = findViewById(R.id.configFloatingButton);
        button.setOnClickListener(View ->{
            Intent intent = new Intent(this, addChildren.class);
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