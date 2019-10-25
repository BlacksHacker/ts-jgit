package com.techsure.tsjgit.util;



import com.techsure.tsjgit.common.Config;
import com.techsure.tsjgit.exception.AuthFailedException;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

public class Authenticator {

	public static boolean authenticate(HttpServletRequest request) throws UnsupportedEncodingException{
		String authorization = request.getHeader("Authorization");
		boolean cando = false;
		if (authorization != null && !authorization.equals("")) {
			authorization = authorization.replace("Basic ", "");
			byte[] bytes = Base64.decodeBase64(authorization);
			authorization = new String(bytes, "UTF-8");
			String[] as = authorization.split(":");
			String username = as[0];
			String password = as[1];
			if (Config.USERNAME.equalsIgnoreCase(username) && Config.PASSWORD.equals(password)) {
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
