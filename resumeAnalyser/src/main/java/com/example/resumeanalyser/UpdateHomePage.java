package com.example.resumeanalyser;

import DAO.RecruitDAO;
import DAO.UserDAO;
import com.mysql.cj.xdevapi.JsonArray;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet("/updateHomePage")
public class UpdateHomePage extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        System.out.println("i am back");

       int totalRecruitsCreated = 0;
       int totalCandidates = 0;
       int totalEmailSend = 0;
       int totalCandidatesRejected =0;
        ArrayList<String> createdDates = new ArrayList<>();
        String sessionId = req.getParameter("sessionId");
       System.out.println("sessionId: " + sessionId);
        if (sessionId != null) {
            try {
                String userId = UserDAO.getUserIdBySessionId(sessionId);
                System.out.println("userId: " + userId);
                JSONArray array = RecruitDAO.getAllRecruits(userId);
                if (array != null) {
                    totalRecruitsCreated = array.length();
                    System.out.println("totalRecruitsCreated: " + totalRecruitsCreated);
                 for (int i = 0; i < array.length(); i++) {
                     JSONObject obj = array.getJSONObject(i);
                     totalCandidates += obj.getInt("total_Resume");
                     totalCandidatesRejected += obj.getInt("rejected");
                     totalEmailSend += obj.getInt("mailSented");
                     createdDates.add(obj.getString("createdAt"));
                 }
                }

                System.out.println("e"+totalRecruitsCreated+"/"+totalCandidates+"/"+totalEmailSend);

                JSONObject obj = new JSONObject();
                obj.put("total_recruits_created", totalRecruitsCreated);
                obj.put("total_candidates", totalCandidates);
                obj.put("total_email_sent", totalEmailSend);
                obj.put("total_candidates_rejected", totalCandidatesRejected);
                obj.put("createdDates", createdDates);
                out.println(obj.toString());

            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else{
            out.print("error");
        }


    }
}
