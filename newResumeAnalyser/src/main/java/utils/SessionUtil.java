package utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;
//import Model.User;

public class SessionUtil {
    public static String getLoggedInUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
//        return (session != null) ? (User) session.getAttribute("loggedInUser") : null;
        	return (session != null) ?  (String) session.getAttribute("userId") : null;
    }
}
