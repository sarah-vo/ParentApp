package ca.cmpt276.parentapp.model;

import java.util.ArrayList;

/**
 * Store a list of Task objects. Supports singleton access and includes methods to
 * edit and retrieve elements from the list. Also has an update task history method which updates the
 * task history when child is removed.
 */

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
                if(history.getChildIndex() > removedChildIndex) {
                    history.setChildIndex(history.getChildIndex()-1);
                }
            }
        }
    }

    public void updateTaskList(Integer removedChildIndex, Integer numChildren) {
        for (Task task:taskList) {
            if (task.getWhoseTurn(numChildren) > removedChildIndex) {
                task.updateTurnToNextChild();
            }
        }
    }
    
    public ArrayList<Task> getTaskList() {
        return taskList;
    }
}




