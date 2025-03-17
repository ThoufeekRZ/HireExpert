package DAO;

import com.mysql.cj.xdevapi.JsonArray;
import okhttp3.*;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Python_model_analyser {
    private static final String MODEL_API_URL = "http://127.0.0.1:5001/analyze-achievement";
    private static final String MODEL_API_Url_sentiment = "http://127.0.0.1:5003/sentiment-analysis";

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)  // ⏳ Increase connection timeout
            .readTimeout(30, TimeUnit.SECONDS)     // ⏳ Increase read timeout
            .writeTimeout(30, TimeUnit.SECONDS)    // ⏳ Increase write timeout
            .build();

    public static int analyseFalseAchievement(List<String> achievements) throws IOException {

        Map<String,List<String>> map = new HashMap<>();
        map.put("achievements",achievements);
        JSONObject jsonObject = new JSONObject(map);

        return api_call(jsonObject, MODEL_API_URL);

    }

    public static int analyseSummary(String summary) throws IOException {


       if(summary != null) {
           JSONObject jsonObject = new JSONObject();
           System.out.println(summary);
           jsonObject.put("text", summary);
           return api_call(jsonObject, MODEL_API_Url_sentiment);
       }
       return 0;
    }

    private static int api_call(JSONObject jsonObject, String modelApiUrlSentiment) throws IOException {
        System.out.println("Sending JSON: " + jsonObject.toString());

        RequestBody requestBody = RequestBody.create(jsonObject.toString(), MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(modelApiUrlSentiment)
                .post(requestBody)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        String responseString = response.body() != null ? response.body().string() : "{}";  // ✅ Prevents NullPointerException
        System.out.println(responseString);

        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }

        JSONObject jsonObject1 = new JSONObject(responseString);

        int score = jsonObject1.getInt("score");
        return score;
    }


}
