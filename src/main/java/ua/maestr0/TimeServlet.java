package ua.maestr0;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@WebServlet(value = "/time")
public class TimeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        try {
            ZonedDateTime now = getZonedDateTime(resp, req.getParameter("timezone"));
            String offsetDisplay = "UTC" + Objects.requireNonNull(now).getOffset();

            String formattedTime = now.format(formatter);
            resp.getWriter().println(formattedTime + " " + offsetDisplay);
            resp.getWriter().flush();
            resp.setStatus(200);
        } catch (IOException e) {
            e.printStackTrace();
            resp.setStatus(500);
        }
    }

    private ZonedDateTime getZonedDateTime(HttpServletResponse resp, String timezone) throws IOException {
        if (timezone != null && !timezone.isEmpty()) {
            String offsetStr = timezone.replace("UTC", "").trim();
            if (Integer.parseInt(offsetStr) > 0) {
                offsetStr = "+" + offsetStr;
            }
            try {
                ZoneOffset offset = ZoneOffset.of(offsetStr);
                return ZonedDateTime.now(offset);
            } catch (Exception e) {
                resp.setContentType("text/plain");
                resp.setCharacterEncoding("UTF-8");
                resp.getWriter().println("Invalid timezone format. Use 'UTC+2' or 'UTC-2'.");
                resp.getWriter().flush();
                resp.setStatus(500);
                return null;
            }
        } else {
            return ZonedDateTime.now(ZoneOffset.UTC);
        }
    }
}
