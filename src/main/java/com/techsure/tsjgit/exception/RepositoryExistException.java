package com.techsure.tsjgit.exception;

/**
 * @program: ts-jgit
 * @description:
 * @create: 2019-10-25 12:14
 **/
public class RepositoryExistException extends RuntimeException{
    private static final long serialVersionUID = 5394542178760755954L;

    @Override
    public String getMessage(){
        return "Repository exist";
    }
}
