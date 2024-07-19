package com.brihaspathee.zeus.exception;

/**
 * Created in Intellij IDEA
 * User: Balaji Varadharajan
 * Date: 13, July 2024
 * Time: 5:42 AM
 * Project: Zeus
 * Package Name: com.brihaspathee.zeus.exception
 * To change this template use File | Settings | File and Code Template
 */
public class EnrollmentSpanNotFoundException extends RuntimeException{

    public EnrollmentSpanNotFoundException(String message) {
        super(message);
    }

    public EnrollmentSpanNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
