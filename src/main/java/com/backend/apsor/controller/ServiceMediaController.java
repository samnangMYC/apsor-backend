package com.backend.apsor.controller;

import com.backend.apsor.repositories.ServiceLocationRepo;
import com.backend.apsor.repositories.ServiceMediaRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/services/medias")
@RequiredArgsConstructor
public class ServiceMediaController {

    private final ServiceMediaRepo serviceMediaRepo;
}
