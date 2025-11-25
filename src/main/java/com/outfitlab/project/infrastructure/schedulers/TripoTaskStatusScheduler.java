package com.outfitlab.project.infrastructure.schedulers;

import com.outfitlab.project.domain.interfaces.repositories.TripoRepository;
import com.outfitlab.project.domain.model.TripoModel;
import com.outfitlab.project.domain.model.TripoTaskStatusResult;
import com.outfitlab.project.infrastructure.model.TripoEntity;
import com.outfitlab.project.infrastructure.repositories.interfaces.TripoJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TripoTaskStatusScheduler {

    private final TripoRepository tripoRepository;
    private final TripoJpaRepository tripoJpaRepository;

    @Scheduled(fixedDelayString = "${tripo.poll.delay-ms:15000}")
    public void pollPendingTasks() {
        List<TripoEntity> tasks = tripoJpaRepository.findByStatusIn(
                List.of(TripoModel.ModelStatus.PENDING, TripoModel.ModelStatus.PROCESSING));

        for (TripoEntity task : tasks) {
            try {
                TripoTaskStatusResult result = tripoRepository.fetchTaskStatus(task.getTaskId());
                String status = result.getStatus();

                if ("success".equalsIgnoreCase(status)) {
                    task.setStatus(TripoModel.ModelStatus.COMPLETED);
                    task.setTripoModelUrl(result.getModelUrl());
                    log.info("Tarea Tripo {} completada.", task.getTaskId());
                } else if ("failed".equalsIgnoreCase(status)) {
                    task.setStatus(TripoModel.ModelStatus.FAILED);
                    task.setErrorMessage(result.getErrorMessage());
                    log.warn("Tarea Tripo {} falló: {}", task.getTaskId(), result.getErrorMessage());
                } else {
                    task.setStatus(TripoModel.ModelStatus.PROCESSING);
                }

                tripoJpaRepository.save(task);
            } catch (Exception e) {
                log.warn("No pudimos actualizar el estado de la tarea Tripo {}: {}", task.getTaskId(), e.getMessage());
            }
        }
    }
}
