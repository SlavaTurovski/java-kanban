package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Epic extends Task {

    protected List<Integer> subtaskId;
    private final ArrayList<Subtask> subTasksList = new ArrayList<>();

    public Epic(String name, String description, Status status) {
        super(name, description, status);
        subtaskId = new ArrayList<>();
    }

    public List<Integer> getSubtaskIdInEpic() {
        return subtaskId;
    }

    public ArrayList<Subtask> getSubTasksList() {
        return subTasksList;
    }

    public TaskType getTaskType() {
        return TaskType.EPIC;
    }

    public void addSubTask(Subtask subTask) {
        subTasksList.add(subTask);
        startTime = getSubTasksList().stream()
                .map(Subtask::getStartTime)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(null);
        duration = Optional.ofNullable(duration)
                .orElse(Duration.ZERO).plus(subTask.duration != null ? subTask.duration : Duration.ZERO);
    }

    public void deleteSubTask(Subtask subTask) {
        subTasksList.remove(subTask);
        startTime = getSubTasksList().stream()
                .map(Subtask::getStartTime)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(null);
        duration = Optional.ofNullable(duration)
                .map(d -> d.minus(subTask.duration != null ? subTask.duration : Duration.ZERO))
                .orElse(Duration.ZERO);
    }

    @Override
    public LocalDateTime getEndTime() {
        return getSubTasksList().stream()
                .map(Subtask::getStartTime)
                .max(LocalDateTime::compareTo)
                .orElse(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtaskId, epic.subtaskId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtaskId);
    }

    @Override
    public String toString() {
        return "Epic {" +
                "id=" + id +
                ", subtaskId=" + subtaskId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }

}