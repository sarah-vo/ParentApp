package ca.cmpt276.parentapp.model;

import java.util.Random;

public class Task {

    private String taskName;
    private int whoseTurn = -1;

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
        if (whoseTurn == -1) {
            Random rand = new Random();
            whoseTurn = rand.nextInt(numChildren);
        }
        else if (whoseTurn >= numChildren) {
            whoseTurn = 0;
        }
        else {
            whoseTurn = (whoseTurn + 1) % numChildren;
        }
        return whoseTurn;
    }

    private int getWhoseTurn() {
        return whoseTurn;
    }

}
