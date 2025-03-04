package manager;

import tasks.*;

public class CSVTaskFormat {

    public static String toString(Task task) {

        StringBuilder result = new StringBuilder()
                .append(task.getId()).append(",")
                .append(task.getTaskType()).append(",")
                .append(task.getName()).append(",")
                .append(task.getStatus()).append(",")
                .append(task.getDescription());

        return result.toString();
    }

    public static String toString(Subtask subtask) {
        return toString((Task) subtask) + "," + subtask.getEpicId();
    }

    public static Task fromString(String value) {

        String[] values = value.split(",");
        final Integer id = Integer.parseInt(values[0]);
        final TaskType taskType = TaskType.valueOf(values[1]);
        final String name = values[2];
        final Status taskState = Status.valueOf(values[3]);
        final String description = values[4];

        if (taskType == TaskType.TASK) {
            Task result = new Task(name, description, taskState);
            result.setId(id);
            return result;

        } else if (taskType == TaskType.EPIC) {
            Epic result = new Epic(name, description, taskState);
            result.setId(id);
            return result;

        } else if (taskType == TaskType.SUBTASK) {
            final int epicId = Integer.parseInt(values[5]);
            Subtask result = new Subtask(name, description, taskState, epicId);
            result.setId(id);
            return result;

        } else {
            return null;
        }
    }

}