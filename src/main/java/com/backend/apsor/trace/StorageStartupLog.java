package com.backend.apsor.trace;

import com.backend.apsor.entities.StorageProps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StorageStartupLog {
    private final StorageProps props;

    @EventListener(ApplicationReadyEvent.class)
    public void logStorage() {
        log.info("Resolved MinIO endpoint = {}", props.endpoint());
        log.info("Resolved bucket        = {}", props.bucket());
    }
}
