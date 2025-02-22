package manager;

import tasks.*;

public class CSVTaskFormat {

    public static String toString(Task task) {
        StringBuilder result = new StringBuilder();
        result.append(
                task.getId() + ","
                + task.getTaskType() + ","
                + task.getName() + ","
                + task.getStatus() + ","
                + task.getDescription());

        if (task.getTaskType() == TaskType.SUBTASK) {
            result.append("," + ((Subtask)task).getEpicId());
        }

        return result.toString();
    }

    public static Task fromString(String value) {

        String[] values = value.split(",");
        int id = Integer.parseInt(values[0]);
        TaskType taskType = TaskType.valueOf(values[1]);
        Status taskState = Status.valueOf(values[3]);

        if (taskType == TaskType.TASK) {
            Task result = new Task(values[2], values[4], taskState);
            result.setId(id);
            return result;

        } else if (taskType == TaskType.EPIC) {
            Epic result = new Epic(values[2], values[4], taskState);
            result.setId(id);
            return result;

        } else if (taskType == TaskType.SUBTASK) {
            final int epicId = Integer.parseInt(values[5]);
            Subtask result = new Subtask(values[2], values[4], taskState, epicId);
            result.setId(id);
            return result;

        } else {
            return null;
        }
    }

}