package com.suman.projectManager.exception;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectCustomMessage {
    private int status;
    private String message;
    private long timeStamp;

    public ProjectCustomMessage(int status, long timeStamp) {
        this.status = status;
        this.timeStamp = timeStamp;
    }

    public ProjectCustomMessage(String message, long timeStamp) {
        this.message = message;
        this.timeStamp = timeStamp;
    }
}
