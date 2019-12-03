package com.techsure.tsjgit.api;

import com.techsure.tsjgit.api.base.CommitBaseApi;
import com.techsure.tsjgit.api.base.RepositoryBaseApi;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @program: jgitservertest
 * @description: git commit 功能性封装
 * @create: 2019-10-23 10:30
 **/
public class CommitApi {

    /**
     * @Description: 返回仓库指定分支指定文件的commit
     *              这里的path要使用Linux路径
     * @Param: [gitPath, revStr, path]
     * @return: java.lang.Iterable<org.eclipse.jgit.revwalk.RevCommit>
     */
    public static Iterable<RevCommit> listCommits(String gitPath, String revStr, String path) throws IOException, GitAPIException{
        try(Repository repository = RepositoryBaseApi.openJGitRepository(gitPath)){
            try(Git git = new Git(repository)){
                return CommitBaseApi.listCommits(git, revStr, repository, path);
            }
        }
    }
}
