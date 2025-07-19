package com.tus.traunreut.service.schedule;

public enum ETaskIds {
    UPDATE_ALL_MATCHES(1),
    UPDATE_SINGLE_MATCH(2),
    UPDATE_MATCH_PLAYERS(3);

    private final int id;

    ETaskIds(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
