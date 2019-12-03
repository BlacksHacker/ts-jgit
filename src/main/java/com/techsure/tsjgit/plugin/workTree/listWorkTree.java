package com.techsure.tsjgit.plugin.workTree;

import com.techsure.tsjgit.api.CommitApi;
import com.techsure.tsjgit.api.RepositoryApi;
import com.techsure.tsjgit.api.base.CommitBaseApi;
import com.techsure.tsjgit.api.base.RepositoryBaseApi;
import com.techsure.tsjgit.dto.JGitCommitVo;
import com.techsure.tsjgit.dto.JGitFileVo;
import com.techsure.tsjgit.dto.JGitHelpVo;
import com.techsure.tsjgit.exception.ParamBlankException;
import com.techsure.tsjgit.plugin.IJGitPlugin;
import com.techsure.tsjgit.util.JGitUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.eclipse.jetty.util.StringUtil;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @program: ts-jgit
 * @description:
 * @create: 2019-10-24 16:25
 **/
@Component
public class listWorkTree implements IJGitPlugin {

    Logger logger = LoggerFactory.getLogger(listWorkTree.class);

    @Override
    public String getId() {
        return "listworktree";
    }

    @Override
    public JSONObject doService(JSONObject jsonObject) {
        JSONObject returnObj = new JSONObject();
        String repoName = jsonObject.optString("repoName");
        String path = jsonObject.optString("path");
        String branchName = jsonObject.optString("branchName");

        try {
            if (JGitUtil.paramBlankCheck(repoName, branchName)){
                throw new ParamBlankException();
            }
            String gitPath = JGitUtil.buildGitPath(repoName);
            File workTree;
            if (StringUtil.isBlank(path)){
                workTree = RepositoryApi.getWorkTree(gitPath);
            }else {
                workTree = new File(JGitUtil.buildRepositoryPath(repoName) + File.separator + path);
            }
            returnObj.put("Data", listWorkTreeFile(gitPath, branchName, workTree, repoName));
            returnObj.put("Status", "OK");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            returnObj.put("Status", "ERROR");
            returnObj.put("Message", e.getMessage());
        }
        return  returnObj;
    }

    private List<JGitFileVo> listWorkTreeFile(String gitPath, String revStr, File workTree, String repositoryName) throws IOException, GitAPIException {
        List<JGitFileVo> jGitFileList = new ArrayList<>();
        String[] childPaths = workTree.list();
        for (String path : childPaths) {
            File file = new File(workTree.getPath() + File.separator +path);
            String relativePath = file.getPath().replace(JGitUtil.buildRepositoryPath(repositoryName) + File.separator, "").replace(File.separator, "/");
            JGitFileVo jgitfileVo = new JGitFileVo();
            jgitfileVo.setPath(relativePath);
            Iterable<RevCommit> commits = CommitApi.listCommits(gitPath, revStr, relativePath);
            Iterator iterator =commits.iterator();
            List<JGitCommitVo> commitList = new ArrayList<>();
            if (iterator.hasNext()){
                RevCommit commit = (RevCommit) iterator.next();
                commitList.add(new JGitCommitVo(commit));
            }
            jgitfileVo.setCommitVos(commitList);
            jGitFileList.add(jgitfileVo);
        }
        return jGitFileList;
    }

    @Override
    public JSONArray help() {
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(new JGitHelpVo("repoName","String", true,"repository name").parseJSON());
        jsonArray.add(new JGitHelpVo("branchName", "String", true, "branch name"));
        jsonArray.add(new JGitHelpVo("path", "String", false, " repository absolute path"));
        return jsonArray;
    }
}
