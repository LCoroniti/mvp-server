package com.tus.traunreut.service.schedule;

public enum ETaskIds {
    UPDATE_ALL_MATCHES(1);

    private final int id;

    ETaskIds(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
