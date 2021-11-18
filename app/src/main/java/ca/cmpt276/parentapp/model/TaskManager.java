package ca.cmpt276.parentapp.model;

import java.util.ArrayList;

public  class TaskManager {
    private static TaskManager instance;
    private final ArrayList<Task> taskList;

    public static TaskManager getInstance(){
        if(instance == null){
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

    public ArrayList<Task> getTaskList() {return taskList;}

    public void addTask(String name){
        taskList.add(new Task(name));
    }

    public void removeTask(int index){
        taskList.remove(index);
    }

    public void editTask(String taskName, int index){
        taskList.get(index).editTaskName(taskName);
    }

    public String getName(int pos) {
        return taskList.get(pos).getTaskName();
    }
}




