package com.revature.security.jwt;

public class JwtConstants {
	
	public static final String SIGNING_KEY = "j3H5Ld5nYmGWyULy6xwpOgfSH++NgKXnJMq20vpfd+8=t";
	public static final long ACCESS_TOKEN_VALIDITY_SECONDS = 5*60*60;
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final String AUTHORITIES_KEY = "scopes";
}
