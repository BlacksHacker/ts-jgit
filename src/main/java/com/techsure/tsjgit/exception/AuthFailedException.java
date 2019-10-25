package com.techsure.tsjgit.exception;

public class AuthFailedException extends RuntimeException{

	private static final long serialVersionUID = 5394542178760755954L;

	@Override
	public String getMessage(){
		return "Authenticate failed";
	}
}
