package com.example.taskmanagementapi.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskTrashCleanupJob {

    private final TaskRepository taskRepository;

    @Value("${task.trash.retention-days}")
    private long retentionDays;

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void cleanOldTrash() {
        LocalDateTime threshold =
                LocalDateTime.now().minusDays(retentionDays);

        log.info("Cleaning tasks deleted before {}", threshold);

        taskRepository.deleteAllByDeletedAtBefore(threshold);
    }
}
