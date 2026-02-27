package com.backend.apsor.controller;

import com.backend.apsor.payloads.dtos.CategoryMediaDTO;
import com.backend.apsor.service.CategoryMediaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/categories/")
@RequiredArgsConstructor
public class CategoryMediaController {

    private final CategoryMediaService categoryMediaService;

    @PostMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CategoryMediaDTO> upload(
            @PathVariable("id") Long categoryId,
            @RequestPart("file") MultipartFile file
    ) {
        log.info("Uploading category media file");
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryMediaService.uploadCategoryMedia(categoryId, file));
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<List<CategoryMediaDTO>> fetchAllById(@PathVariable("id") Long categoryId) {
        log.info("Fetching category media file");
        return ResponseEntity.ok(categoryMediaService.getAllCategoryImgById(categoryId));
    }

    @PutMapping(value = "/{id}/image/{mediaId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CategoryMediaDTO> updateCategoryMedia(
            @PathVariable("id") Long categoryId,
            @PathVariable("mediaId") Long catMediaId,
            @RequestPart("file") MultipartFile file
    ) {
        log.info("Update category media id {}", catMediaId);
        return ResponseEntity.ok(categoryMediaService.updateCategoryMedia(categoryId, catMediaId, file));
    }

    @DeleteMapping("/{id}/image/{mediaId}")
    public ResponseEntity<String> delete(@PathVariable("id") Long categoryId,
                                         @PathVariable("mediaId") Long catMediaId){
        log.info("Delete category media with id {} and cat id {}", categoryId, catMediaId);
        return ResponseEntity.ok(categoryMediaService.deleteCategoryImgByMediaId(categoryId,catMediaId));
    }

}

