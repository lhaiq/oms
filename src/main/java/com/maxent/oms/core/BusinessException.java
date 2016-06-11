

package com.maxent.oms.core;

public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 8136282319218571410L;
    protected final String errorCode;

    public BusinessException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public BusinessException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return this.errorCode;
    }
}
