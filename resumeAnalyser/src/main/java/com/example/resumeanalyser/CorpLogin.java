package com.example.resumeanalyser;

import DAO.CorpLoginPost;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;

@WebServlet("/corpLogin")
public class CorpLogin extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().println("GET request received");
        String code = req.getParameter("code");

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("https://accounts.zoho.com/oauth/v2/token?client_id=1000.KFNDCNGYB48MAD72J7HXP9ZY8HI4TI&client_secret=5f44964528dea1eb759e8e114b57939a832bc99943&grant_type=authorization_code&redirect_uri=http://localhost:8084/resumeAnalyser_war_exploded/corpLogin&code="+code)
                .method("POST", body)
                .addHeader("Cookie", "_zcsr_tmp=dfcdbd35-d903-4805-9c5c-4c1e8a227610; iamcsr=dfcdbd35-d903-4805-9c5c-4c1e8a227610; zalb_b266a5bf57=a7f15cb1555106de5ac96d088b72e7c8")
                .build();
        Response response = client.newCall(request).execute();

        System.out.println(response.body().string());

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().println("POST request received");
    }
}
