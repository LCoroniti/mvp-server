package com.tus.traunreut.events;

import com.tus.traunreut.Match;
import org.springframework.context.ApplicationEvent;

public class MatchUpdateEvent extends ApplicationEvent {
    private final Match match;

    public MatchUpdateEvent(Object source, Match match) {
        super(source);
        this.match = match;
    }

    public Match getMatch() {
        return match;
    }
}
