package ua.maestr0;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;

@WebFilter(value = "/time")
public class TimezoneValidateFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String timezone = servletRequest.getParameter("timezone");
        if (timezone == null) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        String offsetStr = timezone.replace("UTC", "").trim();
        if(Math.abs(Integer.parseInt(offsetStr)) <= 12) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            servletResponse.getWriter().println("Invalid timezone format. MaxValue = UTC±12.");
        }
    }
}
