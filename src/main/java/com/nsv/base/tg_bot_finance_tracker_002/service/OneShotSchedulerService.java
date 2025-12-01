package com.nsv.base.tg_bot_finance_tracker_002.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;

import java.time.Instant;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class OneShotSchedulerService {

    @Autowired
    private TaskScheduler taskScheduler;

    private final AtomicReference<ScheduledFuture<?>> scheduledTask = new AtomicReference<>();

    /**
     * Запускает отложенное выполнение задачи, если ещё не запланирована.
     * @param delayMs задержка в миллисекундах
     * @param task задача для выполнения
     */
    public void scheduleOnce(long delayMs, Runnable task) {
        // Пытаемся установить новую задачу, только если текущая — null или завершена
        scheduledTask.updateAndGet(current -> {
            if (current == null || current.isDone()) {
                ScheduledFuture<?> future = taskScheduler.schedule(
                        () -> {
                            try {
                                task.run();
                            } finally {
                                // Опционально: явно сбросить ссылку после выполнения
                                scheduledTask.set(null);
                            }
                        },
                        Instant.now().plusMillis(delayMs)
                );
                return future;
            }
            // Уже запланирована — оставляем как есть (игнорируем)
            return current;
        });
    }
}