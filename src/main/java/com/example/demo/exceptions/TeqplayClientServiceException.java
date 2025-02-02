package com.example.demo.exceptions;

import com.example.demo.model.teqplay.TeqplayError;
import lombok.Getter;

@Getter
public class TeqplayClientServiceException extends Exception {

    private final int code;
    private final TeqplayError teqplayError;

    public TeqplayClientServiceException(TeqplayError teqplayError) {
        super(teqplayError.getMessage());
        this.code = teqplayError.getCode();
        this.teqplayError = teqplayError;
    }


}
