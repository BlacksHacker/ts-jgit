package com.techsure.tsjgit.api;


import com.techsure.tsjgit.api.base.BranchBaseApi;
import com.techsure.tsjgit.api.base.RepositoryBaseApi;
import com.techsure.tsjgit.exception.MergeConflictException;
import com.techsure.tsjgit.util.DiffUtil;
import com.techsure.tsjgit.util.JGitUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

    /** 
    * @Description: 分支合并 
    * @Param: [gitPath, sourceBra, targetBra] 
    * @return: void  
    */ 
    public static void branchMerge(String gitPath, String sourceBra, String targetBra) throws IOException, GitAPIException{
       try(Repository repository = RepositoryBaseApi.openJGitRepository(gitPath)){
            try(Git git = new Git(repository)){
               MergeResult mergeResult = BranchBaseApi.branchMerge(git, repository, sourceBra, targetBra);
               if (mergeResult.getConflicts() == null){
                    git.commit().setAll(true).setMessage("Merge branch "+ sourceBra + " into " + targetBra).call();
               }else {
                    throw new MergeConflictException();
               }
               /*if (mergeResult.getConflicts() != null){
                   JSONObject conflictObj = new JSONObject();
                   for (Map.Entry<String,int[][]> entry : mergeResult.getConflicts().entrySet()) {
                       StringBuffer buffer = new StringBuffer();
                       for(int[] arr : entry.getValue()) {
                           buffer.append("__");
                           buffer.append(Arrays.toString(arr));
                       }
                       conflictObj.put(entry.getKey(), buffer.toString());
                   }
                   return conflictObj;
               }else {
                   return null;
               }*/
            }
        }
    }

    /** 
    * @Description: 分支冲突检测 
    * @Param: [gitPath, sourceBra, targetBra] 
    * @return: boolean  
    */ 
    public static boolean checkConflict(String gitPath, String sourceBra, String targetBra) throws GitAPIException, IOException{
        try(Repository repository = RepositoryBaseApi.openJGitRepository(gitPath)){
            try(Git git = new Git(repository)){
                MergeResult mergeResult = BranchBaseApi.branchMerge(git, repository, sourceBra, targetBra);
                if (mergeResult.getConflicts() == null){
                    git.checkout().setName(JGitUtil.appendRefHead(sourceBra)).setForced(true).call();
                    return false;
                }
                return true;
            }
        }
    }

    /** 
    * @Description: 分支文件差异 
    * @Param: [gitPath, sourceBra, targetBra, fileName] 
    * @return: net.sf.json.JSONObject  
    */ 
    public static JSONArray diffBranch(String gitPath, String sourceBra, String targetBra, String fileName) throws IOException, GitAPIException{
        JSONArray diffArray = new JSONArray();
        try(Repository repository = RepositoryBaseApi.openJGitRepository(gitPath)){
            try(Git git = new Git(repository)){
                List<DiffEntry> diff = BranchBaseApi.diffBranches(git, sourceBra, targetBra, repository, fileName);
                for (DiffEntry entry : diff) {
                    try(ByteArrayOutputStream stream = new ByteArrayOutputStream()){
                        try (DiffFormatter formatter = new DiffFormatter(stream)) {
                            formatter.setRepository(repository);
                            formatter.format(entry);
                        }
                        diffArray.add(DiffUtil.getDiffCodeJson(stream.toString()));
                    }
                }
            }
        }
        return diffArray;
    }

    /**
    * @Description: 分支切换
    * @Param: [gitPath, branchName]
    * @return: void
    */
    public static void checkoutBranch(String gitPath, String branchName) throws IOException, GitAPIException{
        try(Repository repository = RepositoryBaseApi.openJGitRepository(gitPath)){
            try(Git git = new Git(repository)){
                BranchBaseApi.checkoutBranch(git, branchName);
            }
        }
    }
}
