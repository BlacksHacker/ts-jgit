package com.techsure.tsjgit.api.base;

import org.eclipse.jetty.util.StringUtil;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.IOException;

/**
 * @program: jgitservertest
 * @description: git Commit底层封装
 * @create: 2019-10-23 09:51
 **/
public class CommitBaseApi {

    /*public static Iterable<RevCommit> listCommits(Git git) throws IOException, GitAPIException {
        return git.log()
                .all()
                .call();
    }

    public static Iterable<RevCommit> listCommits(Git git, String revStr, Repository repository) throws IOException, GitAPIException{
        return git.log()
                .add(repository.resolve(revStr))
                .call();
    }

    public static Iterable<RevCommit> listCommits(Git git, String revStr, Repository repository, String path) throws IOException, GitAPIException {
        return git.log()
                .add(repository.resolve(revStr))
                .addPath(path)
                .call();
    }*/

    public static Iterable<RevCommit> listCommits(Git git, String revStr, Repository repository, String path) throws IOException, GitAPIException {
        LogCommand logCommand = git.log();
        if (StringUtil.isNotBlank(revStr)){
            logCommand = logCommand.add(repository.resolve(revStr));
            if (StringUtil.isNotBlank(path)){
                logCommand = logCommand.addPath(path);
            }
        }else {
            logCommand = logCommand.all();
        }
        return logCommand.call();
    }
}
