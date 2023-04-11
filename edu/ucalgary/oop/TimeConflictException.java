package edu.ucalgary.oop;

/**
 * Exception for handling impossible schedules.
 * 
 * @author Zachariah Blair
 * @version 1.0
 * @since 2023-04-01
 * Zachariah Blair, Ken Liu, Sudarshan Naicker, Rutvi Brahmbhatt
 */
public class TimeConflictException extends Exception {
    private Task task;
    public TimeConflictException(Task task) {
        this.task = task;
    }
    public TimeConflictException() {
        this.task = null;
    }

    public Task getTask() {
        return this.task;
    }
}
