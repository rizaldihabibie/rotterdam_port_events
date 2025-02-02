package com.example.demo.model.teqplay;

public enum TeqplayError {

    UNKNOWN_ERROR(1000, "Unknown Error"),
    WRONG_CREDENTIAL(1401, "Wrong Credential");

    private final int code;
    private String message;

    TeqplayError(int code, String message) {
        this.code = code;
        this.message = message;
    }
    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
