package com.backend.apsor.util;

import java.text.Normalizer;
import java.util.*;
import java.util.regex.Pattern;

public final class SlugUtil {
    private SlugUtil() {}

    private static final List<String> PREFERRED_KEYS = List.of("en", "km");
    private static final Pattern DIACRITICS = Pattern.compile("\\p{M}+");

    public static Optional<String> pickDisplayName(Map<String, String> name) {
        if (name == null || name.isEmpty()) return Optional.empty();

        return PREFERRED_KEYS.stream()
                .map(name::get)
                .filter(SlugUtil::hasText)
                .findFirst()
                .or(() -> name.values().stream().filter(SlugUtil::hasText).findFirst());
    }

    public static Optional<String> slugify(String input, int maxLen) {
        if (!hasText(input)) return Optional.empty();

        String s = Normalizer.normalize(input.trim(), Normalizer.Form.NFD);
        s = DIACRITICS.matcher(s).replaceAll("");

        s = s.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-+|-+$)", "");

        if (s.isBlank()) return Optional.empty();

        if (s.length() > maxLen) {
            s = s.substring(0, maxLen).replaceAll("-+$", "");
        }

        return s.isBlank() ? Optional.empty() : Optional.of(s);
    }

    private static boolean hasText(String s) {
        return s != null && !s.isBlank();
    }
}