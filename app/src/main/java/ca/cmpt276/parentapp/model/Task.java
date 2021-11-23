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
        this.taskName = newTaskName;
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

    public Child getCurrentTurnChild() {
        ChildManager childrenManager = ChildManager.getInstance();
        int numChildren = childrenManager.getNumberOfChildren();
        Child child;
        if (numChildren == 0) {
             return null;
        }
        else {
            child = childrenManager.getChild(getWhoseTurn(numChildren));
        }

        return child;
    }

    @NonNull
    @Override
    public String toString() {
        String childName;
        if (getCurrentTurnChild() == null) {
            childName = "No child yet";
        }
        else {
            childName = getCurrentTurnChild().getChildName();
        }
        return taskName + "\nNext turn: " + childName;
    }
}
