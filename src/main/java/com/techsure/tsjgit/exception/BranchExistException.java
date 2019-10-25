package com.techsure.tsjgit.exception;

/**
 * @program: ts-jgit
 * @description:
 * @create: 2019-10-24 15:18
 **/
public class BranchExistException extends RuntimeException {

    private static final long serialVersionUID = 5394542178760755954L;

    @Override
    public String getMessage(){
        return "Branch exist";
    }
}
