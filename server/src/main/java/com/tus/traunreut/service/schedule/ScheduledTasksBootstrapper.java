package com.tus.traunreut.service.schedule;

import com.tus.traunreut.events.InitializeScheduledTasksEvent;
import com.tus.traunreut.events.PrepareScheduledTasksEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("prod")
@Component
public class ScheduledTasksBootstrapper implements ApplicationListener<ApplicationReadyEvent> {
    private final ApplicationEventPublisher publisher;

    public ScheduledTasksBootstrapper(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        // Fire event that the initial scheduled tasks on startup can be persisted
        publisher.publishEvent(new PrepareScheduledTasksEvent());
        // Fire event that schedules all tasks from database to scheduler
        publisher.publishEvent(new InitializeScheduledTasksEvent());
    }
}
