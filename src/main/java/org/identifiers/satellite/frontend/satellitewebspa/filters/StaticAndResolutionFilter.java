package org.identifiers.satellite.frontend.satellitewebspa.filters;


import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Project: satellite-webspa
 * Package: org.identifiers.satellite.frontend.satellitewebspa.filters
 * Timestamp: 2019-05-17 07:29
 *
 * @author Manuel Bernal Llinares <mbdebian@gmail.com>
 * ---
 */
@Component
@Slf4j
@Order(1)
public class StaticAndResolutionFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("Running my custom filter");
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        String path = req.getRequestURI().substring(req.getContextPath().length());
        if (path.contains("/index.html")) {
            log.info(String.format("Delegate to default - path '%s'", path));
            filterChain.doFilter(servletRequest, servletResponse); // Goes to default servlet.
        } else if ((path.startsWith("/devopsApi")) || (path.startsWith("/healthApi"))) {
            log.info(String.format("Delegate to my handlers - path '%s'", path));
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            String newPath = "/resolutionApi" + path;
            log.info(String.format("Sending the request to the resolution API - path '%s'", newPath));
            servletRequest.getRequestDispatcher(newPath).forward(servletRequest, servletResponse); // Goes to your
            // controller.
        }
    }
}
