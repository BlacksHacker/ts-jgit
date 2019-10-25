package com.techsure.tsjgit.api;


import com.techsure.tsjgit.api.base.BranchBaseApi;
import com.techsure.tsjgit.api.base.RepositoryBaseApi;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;

import java.io.IOException;
import java.util.List;

/**
 * @program: jgitservertest
 * @description: git branch 功能性封装
 * @create: 2019-10-23 10:18
 **/
public class BranchApi {


    /**
     * @Description: 根据git文件地址，返回所有分支
     * @Param: [gitPath]
     * @return: java.util.List<org.eclipse.jgit.lib.Ref>
     */
    public static List<Ref> listBranchs(String gitPath) throws IOException, GitAPIException {
        try(Repository repository = RepositoryBaseApi.openJGitRepository(gitPath)){
            try(Git git = new Git(repository)){
                return BranchBaseApi.listBranches(git);
            }
        }
    }


    /**
     * @Description: 从指定点创建分支
     * 该点可以为 HAS码，也可以是 refs/heads/{branchName} 分支名称
     * @Param: [gitPath, branchName, startPoint]
     * @return: void
     */
    public static void branchCreate(String gitPath, String branchName, String startPoint) throws IOException, GitAPIException{
        try(Repository repository = RepositoryBaseApi.openJGitRepository(gitPath)){
            try(Git git = new Git(repository)){
                BranchBaseApi.branchCreate(git, branchName, startPoint);
            }
        }
    }


    /**
     * @Description: 从当前分支创建分支
     * @Param: [gitPath, branchName]
     * @return: void
     */
    public static void branchCreate(String gitPath, String branchName) throws IOException, GitAPIException{
        try(Repository repository = RepositoryBaseApi.openJGitRepository(gitPath)){
            try(Git git = new Git(repository)){
                BranchBaseApi.branchCreate(git, branchName);
            }
        }
    }


    /**
     * @Description: 检测分支是否存在，branchName为直接分支名
     * @Param: [gitPath, branchName]
     * @return: boolean
     */
    public static boolean branchExist(String gitPath, String branchName) throws IOException, GitAPIException{
        try(Repository repository = RepositoryBaseApi.openJGitRepository(gitPath)){
            try(Git git = new Git(repository)){
                return BranchBaseApi.branchExist(git, branchName);
            }
        }
    }


    /**
     * @Description: 删除分支，branchName为直接分支名
     * @Param: [gitPath, branchName]
     * @return: void
     */
    public static void branchDelete(String gitPath, String branchName) throws IOException, GitAPIException{
        try(Repository repository = RepositoryBaseApi.openJGitRepository(gitPath)){
            try(Git git = new Git(repository)){
                BranchBaseApi.branchDelete(git, branchName);
            }
        }
    }
}
