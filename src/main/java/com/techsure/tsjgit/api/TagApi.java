package com.techsure.tsjgit.api;


import com.techsure.tsjgit.api.base.RepositoryBaseApi;
import com.techsure.tsjgit.api.base.TagBaseApi;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.IOException;
import java.util.List;

/**
 * @program: jgitservertest
 * @description: git tag 功能性封装
 * @create: 2019-10-23 10:33
 **/
public class TagApi {


    /**
     * @Description: 创建标签
     * @Param: [gitPath, tagName]
     * @return: void
     */
    public static void tagCreate(String gitPath, String tagName) throws IOException, GitAPIException{
        try(Repository repository = RepositoryBaseApi.openJGitRepository(gitPath)){
            try(Git git = new Git(repository)){
                TagBaseApi.tagCreate(git, tagName);
            }
        }
    }


    /**
     * @Description: 指定位置创建标签
     * @Param: [gitPath, tagName, headHAS]
     * @return: void
     */
    public static void tagCreate(String gitPath, String tagName, String headHAS) throws IOException, GitAPIException {
        try(Repository repository = RepositoryBaseApi.openJGitRepository(gitPath)){
            try(Git git = new Git(repository)){
                TagBaseApi.tagCreate(git, repository, tagName, headHAS);
            }
        }
    }


    /**
     * @Description: 检测标签是否存在，tagName为直接标签名
     * @Param: [gitPath, tagName]
     * @return: boolean
     */
    public static boolean tagExist(String gitPath, String tagName) throws IOException, GitAPIException{
        try(Repository repository = RepositoryBaseApi.openJGitRepository(gitPath)){
            try(Git git = new Git(repository)){
                return TagBaseApi.tagExist(git, tagName);
            }
        }
    }


    /**
     * @Description: 删除指定标签
     * @Param: [gitPath, tagName]
     * @return: void
     */
    public static void tagDelete(String gitPath, String tagName) throws IOException, GitAPIException{
        try(Repository repository = RepositoryBaseApi.openJGitRepository(gitPath)){
            try(Git git = new Git(repository)){
                TagBaseApi.tagDelete(git, tagName);
            }
        }
    }

    public static List<Ref> listTags(String gitPath) throws IOException, GitAPIException{
        try(Repository repository = RepositoryBaseApi.openJGitRepository(gitPath)){
            try(Git git = new Git(repository)){
                return TagBaseApi.listTags(git);
            }
        }
    }

    public static Iterable<RevCommit> listTagCommits(String gitPath, Ref ref) throws IOException, GitAPIException{
        try(Repository repository = RepositoryBaseApi.openJGitRepository(gitPath)){
            try(Git git = new Git(repository)){
                return TagBaseApi.listTagCommits(git, repository, ref);
            }
        }
    }
}
