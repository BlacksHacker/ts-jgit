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
        return "createBranch";
    }

    @Override
    public Object doService(JSONObject jsonObject) {
        String repoName = jsonObject.optString("repoName");
        String branchName = jsonObject.optString("branchName");
        String startPoint = jsonObject.optString("startPoint");
        if (JGitUtil.paramBlankCheck(repoName, branchName, startPoint)){
            throw new ParamBlankException();
        }
        boolean exist = false;
        try{
            exist = BranchApi.branchExist(JGitUtil.buildGitPath(repoName), branchName);
        }catch (Exception ex){
            logger.error(ex.getMessage(), ex);
        }

        if (exist){
            throw new BranchExistException();
        }

        try {
            BranchApi.branchCreate(JGitUtil.buildGitPath(repoName), branchName, startPoint);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public JSONArray help() {
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(new JGitHelpVo("repoName","String", true,"repository name").parseJSON());
        jsonArray.add(new JGitHelpVo("branchName", "String", true, "new branch name"));
        jsonArray.add(new JGitHelpVo("startPoint", "String", true, " source branch name or HAS"));
        return jsonArray;
    }
}
