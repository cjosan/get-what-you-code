package com.cjosan.getwhatyoucode.utils;

public class MailUtils {

	public static String getAccountConfirmationMessage(String username, String token) {
		return String.format(
				"<h3> Welcome to GetWhatYouCode World! </h3>\n" +
				"<p> Hi %s, " +
				"in order to activate your account and start your journey to success by exploring the wonderful world of code, please click on the link below.\n" +
				"</p>" +
				"<p>" +
				"http://localhost:8080/users/email-verification?token=%s" +
				"</p>", username, token);
	}

	public static String getPasswordResetMessage(String username, String token) {
		return String.format(
				"<p> Hi %s, " +
				"You have requested a password reset. Please click on the link below to proceed to password change. If you haven't requested a password reset, please ignore this email.\n" +
				"</p>" +
				"<p>" +
				"http://localhost:8080/users/password-reset-request?token=%s" +
				"</p>", username, token);
	}

}
