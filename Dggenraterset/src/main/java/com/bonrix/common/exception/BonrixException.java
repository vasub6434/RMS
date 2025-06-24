/**************************************************************************************************
 * File Name : BonrixException.java
 * @author   : SAMIR VORA 
 * Version Information : 1.0
 * Customized Exception for all Bonrix Exceptions
 * Modification History:
 * Date		 Modified By		Description 
 *13-02-2018  SAMIR VORA 			User Defined Exception Class
 ************************************************************************************************* */
package com.bonrix.common.exception;


/**
 * Class defines various constructors that can be used for invoking exceptions
 * @author SAMIR VORA
 */
public class BonrixException extends Exception{

	static final long serialVersionUID = -5829545098534135052L;
	private String exceptionMessage;   
    private String errorCode;
    private String loggerMessage;

    /**
     * A public constructor for BonrixException containing no arguments.
     *  
     */
    public BonrixException() {
    }

    /**
     * A public constructor for BonrixException containing two arguments.
     *  1st is any user friendly message and the other is error code,
     *  invoked for checked exceptions.
     */
    
    public BonrixException(String msg, String errorCode) {
    	super(MessageInfoCache.getMessageString(errorCode));
    	this.exceptionMessage = MessageInfoCache.getMessageString(errorCode).equals("")? msg:MessageInfoCache.getMessageString(errorCode);
    	this.loggerMessage = msg;
    	this.errorCode = errorCode;
    }
    
    /**
     * A public constructor for BonrixException containing one argument,
     * invoked for checked exceptions
     *  
     */
    public BonrixException(String errorCode) {
    	super(MessageInfoCache.getMessageString(errorCode));
    	this.exceptionMessage = MessageInfoCache.getMessageString(errorCode).equals("")? errorCode:MessageInfoCache.getMessageString(errorCode);;
    	this.loggerMessage = errorCode;
    	this.errorCode = errorCode;
    }

    /**
     * A public constructor ofBonrixException containing
     * message and root cause (asThrowable) of the exception.
     * Invoked for unchecked exceptions
     */
    public BonrixException(String msg, Throwable e) {
        this.exceptionMessage = msg;
        this.initCause(e);
        this.loggerMessage = msg;
        this.errorCode = MessageInfoCache.getMessageString("err_000");
    }

    /**
     * sets the root cause of the exception. Used for setting Java built in
     * exception in BonrixException.
     *  
     */
    public void setCause(Throwable e) {
        this.initCause(e);
    }

    /**
     * Gets the class name and exception message.
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String s = getClass().getName();
        return s + ": " + exceptionMessage;
    }

    /**
     * Gets the message of the exception. equivalent to Exception.getMessage().
     */
    public String getMessage() {
    	return exceptionMessage;
    }
    
    /**
     * Gets the logger message of the exception.
     */
    public String getLoggerMessage() {
    	return loggerMessage;
    }
    
    /**
     * Gets the errorCode.
     */
    public String getErrorCode() {
    	return errorCode;
    }
}
