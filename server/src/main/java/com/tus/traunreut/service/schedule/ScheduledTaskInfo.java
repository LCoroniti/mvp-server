package com.tus.traunreut.service.schedule;

import java.time.Instant;
import java.util.concurrent.ScheduledFuture;

public record ScheduledTaskInfo(ScheduledFuture<?> future, Instant executionTime) {
}
