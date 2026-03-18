package com.geonho1943.sharemylist.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.net.URI;
import java.util.UUID;

@Component
public class RequestSecurityHelper {
    private static final String SUBMIT_YOUTUBE_LINK_CSRF_TOKEN = "submitYoutubeLinkCsrfToken";
    private static final String CREATE_PLAYLIST_CSRF_TOKEN = "createPlaylistCsrfToken";

    public void addSubmitYoutubeLinkCsrfToken(HttpSession session, Model model) {
        model.addAttribute("csrfToken", getOrCreateCsrfToken(session, SUBMIT_YOUTUBE_LINK_CSRF_TOKEN));
    }

    public void addCreatePlaylistCsrfToken(HttpSession session, Model model) {
        model.addAttribute("csrfToken", getOrCreateCsrfToken(session, CREATE_PLAYLIST_CSRF_TOKEN));
    }

    public void validateSubmitYoutubeLinkRequest(HttpServletRequest request, HttpSession session, String csrfToken) {
        validateSameOrigin(request);
        validateCsrfToken(session, csrfToken, SUBMIT_YOUTUBE_LINK_CSRF_TOKEN);
    }

    public void validateCreatePlaylistRequest(HttpServletRequest request, HttpSession session, String csrfToken) {
        validateSameOrigin(request);
        validateCsrfToken(session, csrfToken, CREATE_PLAYLIST_CSRF_TOKEN);
    }

    private String getOrCreateCsrfToken(HttpSession session, String csrfTokenKey) {
        String csrfToken = (String) session.getAttribute(csrfTokenKey);
        if (csrfToken == null || csrfToken.isBlank()) {
            csrfToken = UUID.randomUUID().toString();
            session.setAttribute(csrfTokenKey, csrfToken);
        }
        return csrfToken;
    }

    private void validateCsrfToken(HttpSession session, String csrfToken, String csrfTokenKey) {
        String savedCsrfToken = (String) session.getAttribute(csrfTokenKey);
        session.removeAttribute(csrfTokenKey);

        if (savedCsrfToken == null || csrfToken == null || csrfToken.isBlank()) {
            throw new IllegalArgumentException("invalidCsrfToken");
        }
        if (!savedCsrfToken.equals(csrfToken)) {
            throw new IllegalArgumentException("invalidCsrfToken");
        }
    }

    private void validateSameOrigin(HttpServletRequest request) {
        String requestSource = request.getHeader("Origin");
        if (requestSource == null || requestSource.isBlank()) {
            requestSource = request.getHeader("Referer");
        }

        if (requestSource == null || requestSource.isBlank()) {
            throw new IllegalArgumentException("invalidSameSiteRequest");
        }

        try {
            URI requestSourceUri = URI.create(requestSource);
            RequestOrigin requestOrigin = RequestOrigin.fromRequest(request);
            if (!requestOrigin.isSameOrigin(requestSourceUri)) {
                throw new IllegalArgumentException("invalidSameSiteRequest");
            }
        } catch (IllegalArgumentException e) {
            if ("invalidSameSiteRequest".equals(e.getMessage())) {
                throw e;
            }
            throw new IllegalArgumentException("invalidSameSiteRequest");
        }
    }

    private static class RequestOrigin {
        private final String scheme;
        private final String host;
        private final int port;

        private RequestOrigin(String scheme, String host, int port) {
            this.scheme = scheme;
            this.host = host;
            this.port = port;
        }

        public static RequestOrigin fromRequest(HttpServletRequest request) {
            String scheme = extractScheme(request);
            String hostHeader = extractHostHeader(request);

            if (hostHeader == null || hostHeader.isBlank()) {
                return new RequestOrigin(
                        scheme,
                        request.getServerName(),
                        normalizePort(scheme, request.getServerPort())
                );
            }

            URI hostUri = URI.create("http://" + hostHeader);
            String host = hostUri.getHost();
            int port = normalizePort(scheme, hostUri.getPort());

            if (host == null || host.isBlank()) {
                throw new IllegalArgumentException("invalidSameSiteRequest");
            }

            return new RequestOrigin(scheme, host, port);
        }

        private static String extractScheme(HttpServletRequest request) {
            String forwardedProto = request.getHeader("X-Forwarded-Proto");
            if (forwardedProto != null && !forwardedProto.isBlank()) {
                return forwardedProto.split(",")[0].trim();
            }
            return request.getScheme();
        }

        private static String extractHostHeader(HttpServletRequest request) {
            String forwardedHost = request.getHeader("X-Forwarded-Host");
            if (forwardedHost != null && !forwardedHost.isBlank()) {
                return forwardedHost.split(",")[0].trim();
            }
            return request.getHeader("Host");
        }

        private boolean isSameOrigin(URI requestSourceUri) {
            String requestSourceScheme = requestSourceUri.getScheme();
            String requestSourceHost = requestSourceUri.getHost();

            if (requestSourceScheme == null || requestSourceHost == null) {
                return false;
            }

            int requestSourcePort = normalizePort(requestSourceScheme, requestSourceUri.getPort());

            return scheme.equalsIgnoreCase(requestSourceScheme)
                    && host.equalsIgnoreCase(requestSourceHost)
                    && port == requestSourcePort;
        }

        private static int normalizePort(String scheme, int port) {
            if (port != -1) {
                return port;
            }
            if ("https".equalsIgnoreCase(scheme)) {
                return 443;
            }
            return 80;
        }
    }
}
