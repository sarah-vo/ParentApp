package ca.cmpt276.parentapp.model;

import static android.content.Context.MODE_PRIVATE;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;

public  class TaskManager {
    private final ArrayList<Task> taskList;
    private static TaskManager instance;

    public static TaskManager getInstance() {
        if(instance == null) {
            instance = new TaskManager();
        }
        return instance;
    }

    public static void setInstance(TaskManager manager) {
        instance = manager;
    }

    public TaskManager() {
        taskList = new ArrayList<>();
    }

    public void addTask(Task task) {
        taskList.add(task);
    }

    public void removeTask(int index) {
        taskList.remove(index);
    }

    public Task getTask(int index) {
        return taskList.get(index);
    }

    public void updateTaskHistoryList(Integer removedChildIndex) {
        for(Task task : taskList) {
            for(History history: task.getTaskHistoryList()) {
                if(history.getChildIndex() == removedChildIndex) {
                    history.setChildIndex(-1);
                }
            }
        }
    }
    
    public ArrayList<Task> getTaskList() {
        return taskList;
    }
}




