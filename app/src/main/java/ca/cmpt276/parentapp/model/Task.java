package ca.cmpt276.parentapp.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Random;

/**
 * Represent tasks in the app. Each task is an object.
 */

public class Task {

    private String taskName;
    private final ArrayList<History> taskHistoryList = new ArrayList<>();
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

        if (numChildren == 0) {
            whoseTurnIndex = -1;
            return whoseTurnIndex;
        }

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

    public ArrayList<History> getTaskHistoryList() {
        return taskHistoryList;
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
