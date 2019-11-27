package com.techsure.tsjgit.api;

import com.techsure.tsjgit.api.base.CommitBaseApi;
import com.techsure.tsjgit.api.base.RepositoryBaseApi;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;

import java.io.File;
import java.io.IOException;

/**
 * @program: ts-jgit
 * @description:
 * @create: 2019-11-27 11:35
 **/
public class RepositoryApi {

    public static File getWorkTree(String gitPath) throws IOException, GitAPIException{
        try(Repository repository = RepositoryBaseApi.openJGitRepository(gitPath)){
            return repository.getWorkTree();
        }
    }
}
