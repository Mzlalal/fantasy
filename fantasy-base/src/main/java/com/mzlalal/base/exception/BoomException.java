package com.mzlalal.base.exception;

/**
 * fantasy统一异常
 *
 * @author Mzlalal
 * @date 2021/5/23 11:43
 **/
public class BoomException extends RuntimeException {

    private static final long serialVersionUID = 8563551183611473050L;

    public BoomException(String message) {
        super(message);
    }
}
