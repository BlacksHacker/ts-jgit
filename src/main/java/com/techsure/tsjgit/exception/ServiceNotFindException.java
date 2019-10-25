package com.techsure.tsjgit.exception;

public class ServiceNotFindException extends RuntimeException{

	private static final long serialVersionUID = 2616528670062558541L;

	@Override
	public String getMessage(){
		return "Service unavailabe";
	}
}
