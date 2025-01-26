package com.tus.traunreut.webserver.service.schedule;

import com.tus.traunreut.webserver.model.Match;

import java.util.Objects;

public class MatchTask implements Task {
    private final Runnable runnable;
    private final Match match;
    private final EMatchTasks action;

    public MatchTask(Match match, EMatchTasks action, Runnable runnable) {
        this.runnable = runnable;
        this.match = match;
        this.action = action;
    }

    @Override
    public String getId() {
        return (match.getHomeTeam().getName() + match.getGuestTeam().getName() + match.getHomeTeam().getLeague().getName() + action.name()).toLowerCase();
    }

    @Override
    public void run() {
        runnable.run();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MatchTask matchTask = (MatchTask) o;
        return matchTask.getId().equals(getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
