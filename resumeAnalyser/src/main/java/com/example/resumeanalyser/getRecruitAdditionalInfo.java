package com.example.resumeanalyser;

import DAO.RecruitDAO;
import DTO.Recruit;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.http.HttpRequest;
import java.sql.SQLException;

public class getRecruitAdditionalInfo extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        int recruitId = Integer.parseInt(req.getParameter("recruitId"));

        try {
            JSONObject recruitAdditionalInfo = RecruitDAO.getRecruitAdditionalDetails(recruitId);
            out.println(recruitAdditionalInfo.toString());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
