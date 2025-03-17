package DAO;

import com.mysql.cj.xdevapi.Client;
import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CorpLoginPost {
    private static final HttpClient httpClient = HttpClient.newHttpClient();



    public static void CorpLoginPost(String code) throws IOException, InterruptedException {
        String url = "https://accounts.zoho.com/oauth/v2/token";
//        RequestBody body = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("apikey", OCR_API_KEY)
//                .addFormDataPart("language", "eng")
//                .addFormDataPart("OCREngine", "2")
//                .addFormDataPart("scale", "true")
//                .addFormDataPart("detectOrientation", "true")
//                .addFormDataPart("isTable", "true")
//                .addFormDataPart("isOverlayRequired", "false")
//                .addFormDataPart("file", resume.getName(),
//                        RequestBody.create(resume, MediaType.parse(fileType)))
//                .build();



        String newUrl =
            "?client_id=1000.KFNDCNGYB48MAD72J7HXP9ZY8HI4TI&client_secret=5f44964528dea1eb759e8e114b57939a832bc99943&grant_type=authorization_code&redirect_uri=http://localhost:8084/resumeAnalyser_war_exploded/corpLogin&code="+code;

        HttpRequest requestApi = HttpRequest.newBuilder().uri(URI.create(url)).header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(newUrl)).build();

        HttpResponse<String> responseApi = httpClient.send(requestApi, HttpResponse.BodyHandlers.ofString());

        System.out.println(responseApi);
        System.out.println(responseApi.body());
        System.out.println(responseApi.statusCode());

        JSONObject jsonObject = new JSONObject(responseApi.body());
        System.out.println(jsonObject);







    }


}
