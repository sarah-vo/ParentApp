package ca.cmpt276.parentapp.model;

import androidx.annotation.NonNull;

import java.util.Random;

public class Task {

    private String taskName;
    private int whoseTurnIndex = -1;

    public Task(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskName() {
        return this.taskName;
    }

    public void setTaskName(String newTaskName) {
        taskName = newTaskName;
    }

    public int getWhoseTurn(int numChildren) {
        if (whoseTurnIndex == -1) {
            Random rand = new Random();
            whoseTurnIndex = rand.nextInt(numChildren);
        }
        else {
            whoseTurnIndex = whoseTurnIndex % numChildren;
        }
        return whoseTurnIndex;
    }

    public void passTurnToNextChild() {
        whoseTurnIndex++;
    }

    public String getCurrentTurnChild() {
        ChildManager childrenManager = ChildManager.getInstance();
        int numChildren = childrenManager.numberOfChildren();
        String name;
        if (numChildren == 0) {
            name = "No child yet";
        }
        else {
            name = childrenManager.getName(getWhoseTurn(numChildren));
        }

        return name;
    }

    @NonNull
    @Override
    public String toString() {
        return taskName + "\nNext turn: " + getCurrentTurnChild();
    }
}
