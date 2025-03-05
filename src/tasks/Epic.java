package tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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