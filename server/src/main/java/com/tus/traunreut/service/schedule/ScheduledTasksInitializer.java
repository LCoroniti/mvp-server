package com.tus.traunreut.service.schedule;

import com.tus.traunreut.events.InitializeScheduledTasksEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasksInitializer implements ApplicationListener<ApplicationReadyEvent> {
    private final ApplicationEventPublisher publisher;

    public ScheduledTasksInitializer(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        publisher.publishEvent(new InitializeScheduledTasksEvent());
    }
}
