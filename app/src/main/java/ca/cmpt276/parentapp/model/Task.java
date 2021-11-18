package ca.cmpt276.parentapp.model;

public class Task {

        String taskName;

        public Task(String taskName) {
            this.taskName = taskName;
        }

        public String getTaskName() {
            return this.taskName;
        }

        public void editTaskName(String newTaskName) {
            taskName = newTaskName;
        }

}
