package com.techsure.tsjgit.exception;

/**
 * @program: ts-jgit
 * @description:
 * @create: 2019-12-02 17:52
 **/
public class MergeConflictException  extends RuntimeException {
    private static final long serialVersionUID = 5394542178760755954L;

    @Override
    public String getMessage(){
        return "merge conflict";
    }
}
