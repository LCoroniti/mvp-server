package com.tus.traunreut.service.schedule;

public interface Task extends Runnable {
    /**
     * Get the ID that identifies a Task.
     */
    String getId();
}
