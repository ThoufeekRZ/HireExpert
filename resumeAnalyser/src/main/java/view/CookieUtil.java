package view;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class CookieUtil {
    public static String getSessionId(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("SESSION_ID".equals(cookie.getName())) { // Change "sessionId" if needed
                    return cookie.getValue();
                }
            }
        }
        return null; // Return null if sessionId cookie is not found
    }
}
