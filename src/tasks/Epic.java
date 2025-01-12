package tasks;

import enums.Status;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task{

    protected List<Integer> subtaskId = new ArrayList<>();

    public Epic(String name, String description, Status status) {
        super(name, description, status);
        subtaskId = new ArrayList<>();
    }

    public List<Integer> getSubtaskIdInEpic() {
        return subtaskId;
    }

}
