package com.cjosan.getwhatyoucode.config.security;

public class SecurityConstants {

	public static final long JWT_EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 3;
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String AUTH_HEADER = "Authorization";
	public static final String TOKEN_SECRET = "5tEW9RxGp8";

	public static final String SIGN_UP_URL = "/users";
	public static final String VERIFICATION_EMAIL_URL = "/users/email-verification";
	public static final String PASSWORD_RESET_REQUEST_URL = "/users/password-reset-request";
	public static final String PASSWORD_RESET_URL = "/users/password-reset";

}
