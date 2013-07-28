package com.renren.api;

public class RenrenApiException extends RuntimeException{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private int errorCode;

    public RenrenApiException(int errorCode, String message) {
        super("ErrorCode:"+errorCode+" "+message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

}
