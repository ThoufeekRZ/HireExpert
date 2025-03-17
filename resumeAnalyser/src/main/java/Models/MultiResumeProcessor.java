package Models;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MultiResumeProcessor {

    private static final String OCR_API_URL = "https://api.ocr.space/parse/image";
    private static final String AI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=AIzaSyBCpYPfI_GCVrGHhWn9iyBS74PmTbzBNd4";
    private static final String OCR_API_KEY = "693274381088957";
    private static final int THREAD_POOL_SIZE = 10;

    private final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(120, TimeUnit.SECONDS)  // Time to establish a connection
            .readTimeout(120, TimeUnit.SECONDS)     // Time to wait for a response
            .writeTimeout(120, TimeUnit.SECONDS)    // Time to send data
            .build();



    // Process a single resume (OCR -> AI API)s
    public CompletableFuture<String> processResume(File resume) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Step 1: Extract text using OCR API
                String extractedText = extractTextFromResume(resume);
                if (extractedText == null) {
                    return null;
                }


                return extractJsonFromText(extractedText);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }, executorService);
    }

    // Step 1: Extract text from resume using OCR API
    private String extractTextFromResume(File resume) throws Exception {
        // Detect file type dynamically
        String fileType = Files.probeContentType(Paths.get(resume.getAbsolutePath()));

        // Default to PDF if file type is unknown
        if (fileType == null) {
            fileType = "application/octet-stream";
        }

        // Create multipart request with dynamic file type
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("apikey", OCR_API_KEY)
                .addFormDataPart("language", "eng")
                .addFormDataPart("OCREngine", "2")
                .addFormDataPart("scale", "true")
                .addFormDataPart("detectOrientation", "true")
                .addFormDataPart("isTable", "true")
                .addFormDataPart("isOverlayRequired", "false")
                .addFormDataPart("file", resume.getName(),
                        RequestBody.create(resume, MediaType.parse(fileType)))
                .build();

        Request request = new Request.Builder()
                .url(OCR_API_URL)
                .post(body)
                .build();

        Response response = executeWithRetry(request,3);
        String responseBody = response.body().string();
        System.out.println("OCR API Response: " + responseBody);  // Debugging

        if (!response.isSuccessful()) {
            throw new IOException("Unexpected response: " + response.code() + " - " + responseBody);
        }

        JSONObject jsonResponse = new JSONObject(responseBody);
        if (!jsonResponse.has("ParsedResults")) {
            throw new JSONException("ParsedResults not found in response.");
        }
        return jsonResponse.getJSONArray("ParsedResults").getJSONObject(0).getString("ParsedText");

    }

    // Step 2: Convert extracted text to structured JSON using AI API
    private String extractJsonFromText(String text) throws Exception {
        JSONObject jsonRequest = new JSONObject()
                .put("contents", Arrays.asList(
                        new JSONObject().put("parts", (Arrays.asList(
                                new JSONObject().put("text", "Extract the following resume text into a structured JSON format with the following fields:  \n"
                                        + "\n"
                                        + "- **Name**  \n"
                                        + "- **Contact Details**:  \n"
                                        + "  - Email  \n"
                                        + "  - Contact Number  \n"
                                        + "  - LinkedIn  \n"
                                        + "  - GitHub  \n"
                                        + "  - Address  \n"
                                        + "\n"
                                        + "- **Skills**: A string array of all relevant skills.  \n"
                                        + "\n"
                                        + "- **Education**: An array containing:  \n"
                                        + "  - Degree  \n"
                                        + "  - University  \n"
                                        + "  - Year of Completion  \n"
                                        + "\n"
                                        + "- **Experience**: An array containing:  \n"
                                        + "  - Company Name  \n"
                                        + "  - Title  \n"
                                        + "  - **YearsOfExperience** (in a real number format) \n"
                                        + "  - **KeyResponsibilities**  \n"
                                        + "\n"
                                        + "- **Volunteering Experience**: An array of organizations the person has volunteered for, along with their role.  \n"+
                                        "- Project Experience (if available)\n" +
                                        "- Soft Skills from the resume\n" +
                                        "- **Summary** : a summary of the resume candidate"+
                                        "- Any Publications, Research, or Patents mentioned\n" +
                                        "- Resume Formatting Quality from these four only no additional text (best/good/better/worst) )"
                                      //  +"- **Grammar Mistakes**: An array containing: all grammar mistakes (note : i am giving you ocr text so some words may not clearly get extracted but you know which words is wrongly written) \n"
                                        + "\n"
                                        + "- **Extra Info**: Any additional relevant details that provide insights into the candidate's career, achievements, certifications, or special recognitions.  \n"
                                        + "\n"
                                        + "Provide the response in **clean, structured JSON format** without unnecessary characters or markdown.  \n"
                                        + "\n\n" + text)
                        )))
                ));

        RequestBody body = RequestBody.create(jsonRequest.toString(), MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(AI_API_URL)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        Response response = executeWithRetry(request,3);;
        String responseJsonString = response.body().string();
        System.out.println("AI API Response: " + responseJsonString);  // Debugging

        if (!response.isSuccessful()) {
            throw new IOException("AI API request failed: " + response.code() + " - " + responseJsonString);
        }

        JSONObject jsonObject = new JSONObject(responseJsonString);

        if (!jsonObject.has("candidates") || jsonObject.getJSONArray("candidates").length() == 0) {
            throw new JSONException("No candidates found in AI API response.");
        }

        JSONArray candidates = jsonObject.getJSONArray("candidates");
        JSONObject content = candidates.getJSONObject(0).optJSONObject("content");

        if (content == null || !content.has("parts") || content.getJSONArray("parts").length() == 0) {
            throw new JSONException("Invalid response structure from AI API.");
        }

        JSONArray parts = content.getJSONArray("parts");
        String extractedText = parts.getJSONObject(0).getString("text");

// Clean the extracted text
        String cleanJson = extractedText.replaceAll("```json", "").replaceAll("```", "").trim();

        try {
            JSONObject parsedJson = new JSONObject(cleanJson);
            return parsedJson.toString(4);
        } catch (JSONException e) {
            throw new JSONException("Failed to parse AI API response as JSON.");
        }

    }

    // Process multiple resumes in parallel
    public List<String> processMultipleResumes(List<File> resumes) {

        int batchSize = 5;
        List<List<File>> batches = new ArrayList<>();
        List<String> jsonStrings = new ArrayList<>();
        for (int i = 0; i < resumes.size(); i += batchSize) {
            batches.add(resumes.subList(i, Math.min(i + batchSize, resumes.size())));
        }

        for (List<File> batch : batches) {
            List<CompletableFuture<String>> futures = batch.stream()
                    .map(this::processResume).collect(Collectors.toList());

            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();


            futures.forEach(future -> {
                try {
                    String result = future.get();
                    if (result != null) {
                        jsonStrings.add(result);
                        System.out.println("Processed Resume JSON:\n" + result);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        executorService.shutdown();
        return jsonStrings;
    }

    private Response executeWithRetry(Request request, int maxRetries) throws IOException, InterruptedException {
        int attempt = 0;
        while (attempt < maxRetries) {
            try {
                return client.newCall(request).execute();
            } catch (SocketTimeoutException e) {
                attempt++;
                if (attempt >= maxRetries) throw e;
                System.out.println("Retrying request... Attempt " + attempt);
                Thread.sleep(2000 * attempt);  // Exponential backoff
            }
        }
        return null;
    }


    public static void main(String[] args) {
        MultiResumeProcessor processor = new MultiResumeProcessor();
        List<File> resumeFiles = new ArrayList<>(Arrays.asList(
                new File("/home/thoufee-zstk375/Downloads/difficult.webp"),
                new File("/home/thoufee-zstk375/Downloads/resume-samples.png"),
                new File("/home/thoufee-zstk375/Downloads/resume.jpg"),
                new File("/home/thoufee-zstk375/Downloads/sampleResume.jpg"),
                new File("/home/thoufee-zstk375/Downloads/difficult.webp")
        ));
        processor.processMultipleResumes(resumeFiles);
    }
}
