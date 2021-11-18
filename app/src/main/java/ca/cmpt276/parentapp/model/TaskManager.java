package ca.cmpt276.parentapp.model;

import java.util.ArrayList;

public  class TaskManager {
    private static TaskManager instance;
    private final ArrayList<Task> taskList;
    public ArrayList<String> taskName = new ArrayList<>();

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
        taskName.add(name);
    }

    public void removeTask(int index){
        taskList.remove(index);
        taskName.remove(index);
    }

    public ArrayList<String> getTaskNameList(){
        return taskName;
    }

    public void editChildren(String name, int index){
        taskList.get(index).editName(name);
        taskName.set(index, name);
    }

    public String getName(int pos) {
        return taskName.get(pos);
    }
}




