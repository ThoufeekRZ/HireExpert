package validation;

import org.json.JSONArray;
import org.json.JSONObject;

import dao.ResumeDAO;

public class ValidateResume {
	public static boolean isValidResume(String jsonString) {
		JSONObject resumeJson=new JSONObject(jsonString);
//		return false;
		if (!resumeJson.has("Name") || resumeJson.optString("Name").trim().isEmpty()) {
            return false;
        }
        if (!resumeJson.has("Education") || resumeJson.optString("Education").trim().isEmpty()) {
            return false;
        }
        
        if (!resumeJson.has("Contact Details")) {
            return false;
        }
        JSONObject contactDetails = resumeJson.optJSONObject("Contact Details");
        if (contactDetails == null || !contactDetails.has("Email") || contactDetails.optString("Email").trim().isEmpty()) {
            return false;
        }

        if (!resumeJson.has("Skills")) {
            return false;
        }
        JSONArray skillsArray = resumeJson.optJSONArray("Skills");
        if (skillsArray == null || skillsArray.length() == 0) {
            return false;
        }

        return ResumeDAO.insertResume(jsonString);
	}
}
