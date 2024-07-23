package ru.maksirep.crackhash.manager.core.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.util.matcher.RequestMatcher;
import ru.maksirep.crackhash.manager.api.ApiPaths;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class DockerRequest implements RequestMatcher {

    private static final String ACCEPTED_URL = ApiPaths.SET_RESULT;
    private static final String DOCKER_ADDR = "worker-service";

    @Override
    public boolean matches(HttpServletRequest request) {
        if (ACCEPTED_URL.contentEquals(request.getRequestURI())) {
            try {
                InetAddress inetAddress = InetAddress.getByName(request.getRemoteAddr());
                return DOCKER_ADDR.equals(inetAddress.getCanonicalHostName().split("\\.")[0]);
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
        } else return false;
    }
}