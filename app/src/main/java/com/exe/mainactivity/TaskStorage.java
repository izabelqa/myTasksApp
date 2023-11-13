package com.exe.mainactivity;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskStorage {
    private static final TaskStorage taskStorage = new TaskStorage();
    private List<Task> tasks;
    public static TaskStorage getInstance() {
        return taskStorage;
    }
    private TaskStorage() {

        tasks = new ArrayList<>();
        for (int i = 1; i <= 150; i++) {
            Task task = new Task();
            if(i%3 == 0) {
                task.setCategory(Category.STUDIES);
            } else {
                task.setCategory(Category.HOME);
            }
            task.setTaskName("Zadanie "+ i + "  ");
            tasks.add(task);
        }
    }

    public void addTask(Task task){
        tasks.add(task);
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public Task getTask(UUID taskId) {
        for (Task task : tasks) {
            if (task.getId().equals(taskId)) {
                return task;
            }
        }
        return null; // Jeśli nie znaleziono zadania o podanym ID
    }
}
