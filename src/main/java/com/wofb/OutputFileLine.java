package com.wofb;

public class OutputFileLine {

    private Integer lineNumber;
    private String message;
    private Status status;

    public OutputFileLine(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public String getMessage() {
        return message;
    }

    public Status getStatus() {
        return status;
    }

    public enum Status {
        OK,
        ERROR
    }

}
