package com.tus.traunreut.service.schedule.executors;

import com.tus.traunreut.ScheduledTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.GenericTypeResolver;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TaskExecutorRegistry {

    private final Map<Class<? extends ScheduledTask>, ScheduledTaskExecutor<?>> executors = new HashMap<>();

    @Autowired
    public TaskExecutorRegistry(List<ScheduledTaskExecutor<?>> executorList) {
        for (ScheduledTaskExecutor<?> executor : executorList) {
            Class<?> executorType = GenericTypeResolver.resolveTypeArgument(executor.getClass(), ScheduledTaskExecutor.class);
            if (executorType != null) {
                executors.put((Class<? extends ScheduledTask>) executorType, executor);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends ScheduledTask> ScheduledTaskExecutor<T> getExecutor(T task) {
        Class<?> taskClass = ClassUtils.getUserClass(task);
        return (ScheduledTaskExecutor<T>) executors.get(taskClass);
    }
}
