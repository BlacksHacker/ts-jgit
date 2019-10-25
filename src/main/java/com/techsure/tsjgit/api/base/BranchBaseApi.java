package com.techsure.tsjgit.api.base;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.MergeCommand;
import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;

import java.io.IOException;
import java.util.List;

/**
 * @program: jgitservertest
 * @description: git Branch底层封装
 * @create: 2019-10-23 09:54
 **/
public class BranchBaseApi {

    private static final String REFS_HEAD = "refs/heads/";

    public static void branchCreate(Git git, String branchName) throws IOException, GitAPIException{
        git.branchCreate()
                .setName(branchName)
                .call();
    }

    public static void branchCreate(Git git, String branchName, String startPoint) throws IOException, GitAPIException{
        git.branchCreate()
                .setName(branchName)
                .setStartPoint(startPoint)
                .call();
    }

    public static void branchDelete(Git git, String branchName) throws IOException, GitAPIException{
        git.branchDelete()
                .setBranchNames(branchName)
                .setForce(true)
                .call();
    }

    public static boolean branchExist(Git git, String branchName) throws IOException, GitAPIException{
        branchName = REFS_HEAD + branchName;
        List<Ref> refs = git.branchList().call();
        for (Ref ref : refs){
            if (branchName.equals(ref.getName())){
                return true;
            }
        }
        return false;
    }

    public static MergeResult mergeBranch(Git git, Repository repository, String sourceBranchName, String message) throws IOException, GitAPIException{
        ObjectId mergeBase = repository.resolve(sourceBranchName);
        return git.merge()
                .include(mergeBase)
                .setCommit(true)
                .setFastForward(MergeCommand.FastForwardMode.NO_FF)
                .setMessage(message)
                .call();
    }

    public static List<Ref> listBranches(Git git) throws IOException, GitAPIException {
        return git.branchList().call();
    }
}
