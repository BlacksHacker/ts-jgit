package com.techsure.tsjgit.util;

import com.techsure.tsjgit.exception.AuthFailedException;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Component
public class Authenticator {

	private static String username;

	private static String password;

	@Value("${auth.name}")
	public  void setUsername(String username) {
		Authenticator.username = username;
	}

	@Value("${auth.password}")
	public  void setPassword(String password){
		Authenticator.password = password;
	}

	public static boolean authenticate(HttpServletRequest request) throws UnsupportedEncodingException{
		String authorization = request.getHeader("Authorization");
		boolean cando = false;
		if (authorization != null && !authorization.equals("")) {
			authorization = authorization.replace("Basic ", "");
			byte[] bytes = Base64.decodeBase64(authorization);
			authorization = new String(bytes, "UTF-8");
			String[] as = authorization.split(":");
			String user_name = as[0];
			String pass_word = as[1];
			if (username.equalsIgnoreCase(user_name) && password.equals(pass_word)) {
				cando = true;
			} else {
				throw new AuthFailedException();
			}
		} else {
			throw new AuthFailedException();
		}

		return cando;
	}
}
