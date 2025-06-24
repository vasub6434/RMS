/**************************************************************************************************
 * File Name : BonrixException.java
 * @author   : SAMIR VORA 
 * Version Information : 1.0
 * Customized Exception for all Easy Pay Exceptions
 * Modification History:
 * Date		 Modified By		Description 
 *13-02-2018  SAMIR VORA 			User Defined Exception Class
 ************************************************************************************************* */
package com.bonrix.common.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Class defines method and gets the detailed message along with the stack trace
 * @author SAMIR VORA
 */
public class ExceptionUtil {

	 /**
     * The length of exception trace beyond which it will be truncated
     */
    public static final int EXCEPTION_TRACE_LENGTH = 4000;

    private static final int INIT_BUFFER_SIZE = 1024;

    /**
     * Gets the stack trace of a BonrixException in String form.
     * 
     * @param exception- BonrixException object
     * @return String- Returns the detailed message.
     *  
     */
    public static String getDetailedMessage(BonrixException exception) {
        StringBuffer msg = new StringBuffer(INIT_BUFFER_SIZE);

        if (exception.getMessage() != null) {
            msg.append("Message : ");
            msg.append(exception.getMessage());
            msg.append("\n");
        }

        msg.append("Exception Stack Trace\n");
        try {
            StringWriter sw = new StringWriter(INIT_BUFFER_SIZE);
            PrintWriter pw = new PrintWriter(sw);
            exception.printStackTrace(pw);
            msg.append(sw.toString());
            sw.close();
        } catch (Exception e) {
            msg.append(exception.toString());
        }
        Throwable rootCause = exception.getCause();
        if (rootCause != null) {
            msg.append("\n Root Exception Stack Trace : ");
            msg.append(rootCause.toString());
            msg.append("\n");
            try {
                StringWriter sw = new StringWriter(INIT_BUFFER_SIZE);
                PrintWriter pw = new PrintWriter(sw);
                rootCause.printStackTrace(pw);
                msg.append(sw.toString());
                sw.close();
            } catch (Exception e) {
                msg.append(rootCause.toString());
            }
        }
        return msg.toString();
    }
	
}
