package ca.cmpt276.parentapp.model;

import java.util.Random;

public class Task {

    private String taskName;
    private int childIndex = -1;

    public Task(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskName() {
        return this.taskName;
    }

    public void editTaskName(String newTaskName) {
        taskName = newTaskName;
    }

    private int setWhoseTurn(int numChildren) {
        if (childIndex == -1) {
            Random rand = new Random();
            childIndex = rand.nextInt(numChildren);
        }
        else if (childIndex >= numChildren) {
            childIndex = 0;
        }
        else {
            childIndex = (childIndex + 1) % numChildren;
        }
        return childIndex;
    }

    private int getWhoseTurn() {
        return childIndex;
    }

}
