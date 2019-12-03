package com.techsure.tsjgit.plugin.file;

import com.techsure.tsjgit.api.BranchApi;
import com.techsure.tsjgit.api.RepositoryApi;
import com.techsure.tsjgit.api.base.RepositoryBaseApi;
import com.techsure.tsjgit.dto.JGitHelpVo;
import com.techsure.tsjgit.exception.ParamBlankException;
import com.techsure.tsjgit.plugin.IJGitPlugin;
import com.techsure.tsjgit.util.JGitUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileExistsException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;


/**
 * @program: ts-jgit
 * @description:
 * @create: 2019-11-27 16:47
 **/
@Component
public class AddFile implements IJGitPlugin {

    Logger logger = LoggerFactory.getLogger(AddFile.class);

    @Override
    public String getId() {
        return "addfile";
    }

    @Override
    public JSONObject doService(JSONObject jsonObject) {
        JSONObject returnObj = new JSONObject();
        String repoName = jsonObject.optString("repoName");
        String oldBranchName = jsonObject.optString("oldBranchName");
        String newBranchName = jsonObject.optString("newBranchName");
        String content = jsonObject.optString("content");
        String message = jsonObject.optString("message");
        String fileName = jsonObject.optString("fileName");
        String path = jsonObject.optString("path");
        Boolean hasPower = jsonObject.optBoolean("power");

        try {
            if (JGitUtil.paramBlankCheck(repoName, oldBranchName, message, fileName, path)){
                throw new ParamBlankException();
            }
            String fileFullPath = JGitUtil.buildFileFullPath(repoName, fileName, path);
            String relativePath = new StringBuffer()
                    .append(path)
                    .append(File.separator)
                    .append(fileName)
                    .toString();
            File file = new File(fileFullPath);
            if (file.exists()){
                throw new FileExistsException();
            }
            String gitPath = JGitUtil.buildGitPath(repoName);
            //是否有权限直接提交
            if (hasPower){
                BranchApi.checkoutBranch(gitPath, oldBranchName);
            }else {
                if (JGitUtil.paramBlankCheck(newBranchName)){
                    throw new ParamBlankException();
                }
                BranchApi.branchCreate(gitPath, newBranchName, oldBranchName);
                BranchApi.checkoutBranch(gitPath, newBranchName);
            }
            file.createNewFile();
            try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"))){
                bw.write(content);
            }
            RepositoryApi.commitFile(gitPath, message, JGitUtil.toLinux(relativePath));
            returnObj.put("Status", "OK");

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            returnObj.put("Status", "ERROR");
            returnObj.put("Message", e.getMessage());
        }
        return returnObj;
    }

    @Override
    public JSONArray help() {
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(new JGitHelpVo("repoName","String", true,"repository name").parseJSON());
        jsonArray.add(new JGitHelpVo("oldBranchName", "String", true, "branchName of from").parseJSON());
        jsonArray.add(new JGitHelpVo("newBranchName", "String", true, "new branchName").parseJSON());
        jsonArray.add(new JGitHelpVo("message", "String", true, "commit message").parseJSON());
        jsonArray.add(new JGitHelpVo("fileName", "String", true, "file name").parseJSON());
        jsonArray.add(new JGitHelpVo("path", "String", true, "belong path").parseJSON());
        return jsonArray;
    }
}
