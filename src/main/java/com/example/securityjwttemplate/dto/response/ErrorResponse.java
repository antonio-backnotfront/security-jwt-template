package com.example.securityjwttemplate.dto.response;

public class ErrorResponse {
    private int status;
    private Object error;
    private long timestamp;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public ErrorResponse(int status, Object error, long timestamp) {
        this.status = status;
        this.error = error;
        this.timestamp = timestamp;
    }
}
