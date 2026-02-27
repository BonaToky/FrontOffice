package com.projet.frontoffice.interceptor;

import com.projet.frontoffice.annotation.CheckToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    private final RestTemplate restTemplate;

    @Value("${backoffice.token.validation.url}")
    private String validationUrl;

    public TokenInterceptor(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            
            CheckToken checkToken = handlerMethod.getMethodAnnotation(CheckToken.class);
            if (checkToken == null) {
                checkToken = handlerMethod.getBeanType().getAnnotation(CheckToken.class);
            }

            if (checkToken != null) {
                String token = extractToken(request);
                
                if (token == null || token.isEmpty()) {
                    throw new RuntimeException("Accès refusé : Token manquant");
                }

                // Validation réelle auprès du BackOffice
                if (!validateWithBackOffice(token)) {
                    throw new RuntimeException("Accès refusé : Token invalide ou expiré (Vérifié par le BackOffice)");
                }
            }
        }
        return true;
    }

    private boolean validateWithBackOffice(String token) {
        try {
            // On appelle l'URL de validation du BackOffice en passant le token
            String url = validationUrl + "?token=" + token;
            
            // Le BackOffice renvoie du JSON si le token est valide
            // Si le token est invalide, le SecurityFilter du BackOffice renvoie du HTML (page d'erreur)
            // ce qui provoquera une exception lors de la conversion en Map.
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            
            return response != null && response.containsKey("token");
        } catch (Exception e) {
            // Si une erreur survient (ex: 403, 401, ou erreur de parsing JSON), le token est considéré comme invalide
            return false;
        }
    }

    private String extractToken(HttpServletRequest request) {
        // 1. Essayer le header Authorization
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        
        // 2. Essayer le paramètre de requête
        return request.getParameter("token");
    }
}
