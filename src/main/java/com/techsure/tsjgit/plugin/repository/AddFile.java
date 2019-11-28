package com.techsure.tsjgit.plugin.repository;

import com.techsure.tsjgit.api.BranchApi;
import com.techsure.tsjgit.api.RepositoryApi;
import com.techsure.tsjgit.dto.JGitHelpVo;
import com.techsure.tsjgit.exception.ParamBlankException;
import com.techsure.tsjgit.plugin.IJGitPlugin;
import com.techsure.tsjgit.util.JGitUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @program: ts-jgit
 * @description:
 * @create: 2019-11-27 16:47
 **/
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
        String message = jsonObject.optString("message");
        String fileName = jsonObject.optString("fileName");
        String path = jsonObject.optString("path");
        try {
            if (JGitUtil.paramBlankCheck(repoName, oldBranchName, newBranchName, fileName, path)){
                throw new ParamBlankException();
            }
            String gitPath = JGitUtil.buildGitPath(repoName);
            BranchApi.branchCreate(gitPath, newBranchName, oldBranchName);
            RepositoryApi.commitFile(gitPath, message, fileName, path);
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
