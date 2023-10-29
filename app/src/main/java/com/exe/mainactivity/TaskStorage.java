package com.exe.mainactivity;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskStorage {
    private static TaskStorage instance;
    private List<Task> taskList;

    private TaskStorage() {

        taskList = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            Task task = new Task();
            task.setTaskName("Zadanie " + i);
            task.setDone(i%3 == 0);
            taskList.add(task);
        }
    }

    public static TaskStorage getInstance() {
        if (instance == null) {
            instance = new TaskStorage();
        }
        return instance;
    }

    public List<Task> getAllTasks() {
        return taskList;
    }

    public Task getTask(UUID taskId) {
        for (Task task : taskList) {
            if (task.getId() == taskId) {
                return task;
            }
        }
        return null; // Jeśli nie znaleziono zadania o podanym ID
    }
}
