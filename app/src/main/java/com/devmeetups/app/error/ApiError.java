package com.devmeetups.app.error;

import java.time.Instant;
import java.util.Map;

public record ApiError(
        String type, // URI ou code ("https://errors.devmeetups.app/validation" ou "validation")
        String title, // résumé lisible
        int status, // HTTP status
        String detail, // message détaillé (optionnel)
        String instance, // path de la requête (ex: /events)
        Instant timestamp, // horodatage
        Map<String, Object> extras // ex: { "violations": [...] }
) {
}
