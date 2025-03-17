package com.example.resumeanalyser;

import DAO.ResumeDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/changeStatus")
public class ChangeResumeStatus extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        String status = req.getParameter("status");
        int recruitId = Integer.parseInt(req.getParameter("recruitId"));

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter out = resp.getWriter();


        try{
            ResumeDAO.updateResumeStatus(id,recruitId,status);
            out.println("{\"statusChanged\":\""+true+"\"}");
        } catch (SQLException e) {
            e.printStackTrace();
            out.println("{\"statusChanged\":\""+false+"\"}");
        }
    }
}
