package com.techsure.tsjgit.exception;

/**
 * @program: ts-jgit
 * @description:
 * @create: 2019-10-23 18:40
 **/
public class ParamBlankException extends RuntimeException {
    private static final long serialVersionUID = 5394542178760755954L;

    @Override
    public String getMessage(){
        return "Param Empty";
    }
}
