package com.platform.testing.common;

/**
 * @program: ServiceException
 * @description:
 * @author: zhuruiqi
 * @create: 2022-03-16 21:09
 **/
public class ServiceException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private String message;

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ServiceException(final String message, Throwable th) {
        super(message, th);
    }

    public ServiceException(final String message) {
        this.message = message;
    }

    public static void throwEx(String message) {
        throw new ServiceException(message);
    }


}
