package com.techsure.tsjgit.plugin.branch;

import com.techsure.tsjgit.api.BranchApi;
import com.techsure.tsjgit.dto.JGitHelpVo;
import com.techsure.tsjgit.exception.ParamBlankException;
import com.techsure.tsjgit.plugin.IJGitPlugin;
import com.techsure.tsjgit.util.JGitUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @program: ts-jgit
 * @description:
 * @create: 2019-10-24 15:31
 **/
@Component
public class DeleteBranch implements IJGitPlugin {

    Logger logger = LoggerFactory.getLogger(DeleteBranch.class);

    @Override
    public String getId() {
        return "deletebranch";
    }

    @Override
    public JSONObject doService(JSONObject jsonObject) {
        JSONObject returnObj = new JSONObject();
        String repoName = jsonObject.optString("repoName");
        String branchName = jsonObject.optString("braName");
        String mainBraName = jsonObject.optString("mainBraName");
        try {
            if (JGitUtil.paramBlankCheck(repoName, branchName, mainBraName)){
                throw new ParamBlankException();
            }
            String gitPath = JGitUtil.buildGitPath(repoName);
            BranchApi.checkoutBranch(gitPath, mainBraName);
            BranchApi.branchDelete(gitPath, branchName);
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
        jsonArray.add(new JGitHelpVo("braName", "String", true, "delete branch name or HAS"));
        return jsonArray;
    }
}
