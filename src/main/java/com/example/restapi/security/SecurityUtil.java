package com.example.restapi.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    /** Devuelve el email (subject) almacenado en el JWT ya validado. */
    public static String getCurrentUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Refactor explícito para que JaCoCo detecte mejor las ramas
        String email = null;
        if (auth != null) {
            email = (String) auth.getPrincipal();
        }

        return email;
    }
}
