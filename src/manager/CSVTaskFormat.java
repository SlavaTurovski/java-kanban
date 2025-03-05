package manager;

import tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CSVTaskFormat {

    private static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("dd.MM.yyyy | HH:mm");

    public static String toString(Task task) {

        StringBuilder result = new StringBuilder()
                .append(task.getId()).append(",")
                .append(task.getTaskType()).append(",")
                .append(task.getName()).append(",")
                .append(task.getStatus()).append(",")
                .append(task.getDescription()).append(",")
                .append(task.getDuration() != null ? task.getDuration().toMinutes() : " ").append(",")
                .append(task.getStartTime() != null ? task.getStartTime().format(DATE_TIME) : " ").append(",");


        return result.toString();
    }

    public static String toString(Subtask subtask) {
        return toString((Task) subtask) + subtask.getEpicId();
    }

    public static Task fromString(String value) {

        String[] values = value.split(",");
        final Integer id = Integer.parseInt(values[0]);
        final TaskType taskType = TaskType.valueOf(values[1]);
        final String name = values[2];
        final Status status = Status.valueOf(values[3]);
        final String description = values[4];
        final Duration duration = values[5].equals(" ") ?
                null : Duration.ofMinutes(Long.parseLong(values[5]));
        final LocalDateTime startTime = values[6].equals(" ") ?
                null : LocalDateTime.parse(values[6], DATE_TIME);

        if (taskType == TaskType.TASK) {
            Task result = new Task(name, description, status, duration, startTime);
            result.setId(id);
            return result;

        } else if (taskType == TaskType.EPIC) {
            Epic result = new Epic(name, description, status);
            result.setId(id);
            result.getEndTime();
            return result;

        } else if (taskType == TaskType.SUBTASK) {
            final int epicId = Integer.parseInt(values[7]);
            Subtask result = new Subtask(name, description, status, duration, startTime, epicId);
            result.setId(id);
            return result;

        } else {
            return null;
        }
    }

}