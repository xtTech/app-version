package com.tairanchina.csp.avm.exception;

/**
 * 描述:
 *  正则匹配出错异常
 * @author hzds
 * @Create 2018-09 : 11 14:26
 */
public class RegexMatchIllException extends Exception{
    public RegexMatchIllException() {
        super();
    }

    public RegexMatchIllException(String message) {
        super(message);
    }

    public RegexMatchIllException(Throwable cause) {
        super(cause);
    }

    public RegexMatchIllException(String message, Throwable cause) {
        super(message, cause);
    }
}
