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

    /**
    * @Description: 获取文件树
    * @Param: [gitPath]
    * @return: java.io.File
    */
    public static File getWorkTree(String gitPath) throws IOException, GitAPIException{
        try(Repository repository = RepositoryBaseApi.openJGitRepository(gitPath)){
            return repository.getWorkTree();
        }
    }

    /** 
    * @Description: 指定文件commit提交 
    * @Param: [gitPath, message, fileFullPath] 
    * @return: void  
    */ 
    public static void commitFile(String gitPath, String message, String relativePath) throws IOException, GitAPIException{
        try(Repository repository = RepositoryBaseApi.openJGitRepository(gitPath)){
            try(Git git = new Git(repository)){
                RepositoryBaseApi.commitFile(git, message, relativePath);
            }
        }
    }
}
