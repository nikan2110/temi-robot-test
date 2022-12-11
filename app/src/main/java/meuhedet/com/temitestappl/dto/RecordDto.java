package meuhedet.com.temitestappl.dto;

import java.time.LocalDateTime;

public class RecordDto {

    String numberOfQueue;
    LocalDateTime timeOfQueue;

    public String getNumberOfQueue() {
        return numberOfQueue;
    }

    public LocalDateTime getTimeOfQueue() {
        return timeOfQueue;
    }

    public RecordDto() {
    }
}
