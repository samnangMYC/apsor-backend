package com.backend.apsor.helper;

import org.springframework.stereotype.Component;

import java.text.Normalizer;

@Component
public class EntityGenerator {

    public static String generateSlug(String name) {
        String normalized = Normalizer.normalize(name, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{InCombiningDiacriticalMarks}", "")
                .replaceAll("[^\\w\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .toLowerCase()
                .replaceAll("^-|-$", "");
    }
}
