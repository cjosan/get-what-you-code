package com.cjosan.getwhatyoucode.utils;

import com.cjosan.getwhatyoucode.config.security.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class TokenUtils {

	public static String generateToken(String username) {
		return Jwts.builder()
				.setSubject(username)
				.setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.JWT_EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512,SecurityConstants.TOKEN_SECRET)
				.compact();
	}

	public static boolean isTokenExpired(String token) {
		Claims claims = Jwts.parser()
				.setSigningKey(SecurityConstants.TOKEN_SECRET)
				.parseClaimsJws(token)
				.getBody();

		Date tokenExpirationDate = claims.getExpiration();

		return tokenExpirationDate.before(new Date());
	}
}
