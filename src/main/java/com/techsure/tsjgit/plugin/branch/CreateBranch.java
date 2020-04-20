package com.techsure.tsjgit.plugin.branch;

import com.techsure.tsjgit.api.BranchApi;
import com.techsure.tsjgit.dto.JGitHelpVo;
import com.techsure.tsjgit.exception.BranchExistException;
import com.techsure.tsjgit.exception.ParamBlankException;
import com.techsure.tsjgit.plugin.IJGitPlugin;
import com.techsure.tsjgit.util.JGitUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @program: ts-jgit
 * @description:
 * @create: 2019-10-24 14:25
 **/
@Component
public class CreateBranch implements IJGitPlugin {

    Logger logger = LoggerFactory.getLogger(CreateBranch.class);

    @Override
    public String getId() {
        return "createbranch";
    }

    @Override
    public JSONObject doService(JSONObject jsonObject) {
        JSONObject returnObj = new JSONObject();
        String repoName = jsonObject.optString("repoName");
        String branchName = jsonObject.optString("braName");
        String startPoint = jsonObject.optString("startPoint");
        try{
           /* if (JGitUtil.paramBlankCheck(repoName, branchName, startPoint)){
                throw new ParamBlankException();
            }*/
            if (BranchApi.branchExist(JGitUtil.buildGitPath(repoName), branchName)){
                throw new BranchExistException();
            }
            BranchApi.branchCreate(JGitUtil.buildGitPath(repoName), branchName, startPoint);
            returnObj.put("Status", "OK");
        }catch (Exception ex){
            logger.error(ex.getMessage(), ex);
            returnObj.put("Status", "ERROR");
            returnObj.put("Message", ex.getMessage());
        }
        return returnObj;
    }

    @Override
    public JSONArray help() {
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(new JGitHelpVo("repoName","String", true,"repository name").parseJSON());
        jsonArray.add(new JGitHelpVo("braName", "String", true, "new branch name"));
        jsonArray.add(new JGitHelpVo("startPoint", "String", true, " source branch name or HAS"));
        return jsonArray;
    }
}
