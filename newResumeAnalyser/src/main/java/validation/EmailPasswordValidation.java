package validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailPasswordValidation {
	public static boolean isValid(String email, String password) {
		String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
		Pattern pattern1 = Pattern.compile(emailRegex);
		Matcher matcher1 = pattern1.matcher(email);

		boolean isValidEmail = matcher1.matches();

		String passwordRegex = "^(?=.*[0-9])" + "(?=.*[a-z])" + "(?=.*[A-Z])" + "(?=.*[@#$%^&+=!])" + "(?=\\S+$).{8,}$";
		Pattern pattern2 = Pattern.compile(passwordRegex);
		Matcher matcher2 = pattern2.matcher(password);

		boolean isValidPassword = matcher2.matches();

		return isValidEmail && isValidPassword;
	}
}
