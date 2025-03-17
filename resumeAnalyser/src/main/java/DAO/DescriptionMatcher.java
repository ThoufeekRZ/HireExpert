package DAO;

import DTO.Experience;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.mysql.cj.xdevapi.Client;
import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.sql.ClientInfoStatus;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DescriptionMatcher {

    private static final String MODEL_API_URL = "http://127.0.0.1:5005/match";

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)  // ⏳ Increase connection timeout
            .readTimeout(30, TimeUnit.SECONDS)     // ⏳ Increase read timeout
            .writeTimeout(30, TimeUnit.SECONDS)    // ⏳ Increase write timeout
            .build();


    public static int scoreTitleDescriptionAndExperience(String candidateTitle,String jobTitle, List<String> candidateDescription,String jobDescription,int jobExperienceYears, int candidateExperienceYears) throws IOException {

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("candidateTitle",candidateTitle);
        jsonObject.put("jobTitle",jobTitle);
        jsonObject.put("candidateDescription",candidateDescription);
        jsonObject.put("jobDescription",jobDescription);



        RequestBody body = RequestBody.create(jsonObject.toString(),MediaType.parse("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(MODEL_API_URL)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        Response response = client.newCall(request).execute();
        String responseString = response.body().string();
        System.out.println(responseString);

        if(!response.isSuccessful()){
            throw new IOException("Unexpected response: " + response.code() + " - " + responseString);
        }

        JSONObject json = new JSONObject(responseString);
        int score = json.getInt("score");
        int differenceInYears = candidateExperienceYears - jobExperienceYears;

        if (score >= 80) {
            if (differenceInYears >= 5) {
                score += 5;
            } else if (differenceInYears >= 3) {
                score += 3;
            } else {
                score += 1;
            }
        }
        else if (score >= 60) {
            if (differenceInYears >= 5) {
                score += 4;
            } else if (differenceInYears >= 3) {
                score += 2;
            } else {
                score += 1;
            }
        }
        else if (score >= 40) {
            if (differenceInYears >= 5) {
                score += 3;
            } else if (differenceInYears >= 3) {
                score += 1;
            }
        }
        else {
            score -= differenceInYears;
        }

        return score;

    }

    public static  int getExperienceYears(List<Experience> experiences) {
        int year = 0;
        for(Experience experience : experiences) {
            year+= experience.getExperienceYear();
        }
        return year;
    }

}
