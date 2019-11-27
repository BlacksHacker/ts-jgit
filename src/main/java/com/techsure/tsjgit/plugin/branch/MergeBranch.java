package com.techsure.tsjgit.plugin.branch;

import com.techsure.tsjgit.api.BranchApi;
import com.techsure.tsjgit.dto.JGitHelpVo;
import com.techsure.tsjgit.exception.ParamBlankException;
import com.techsure.tsjgit.plugin.IJGitPlugin;
import com.techsure.tsjgit.util.JGitUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

/**
 * @program: ts-jgit
 * @description: 合并分支
 * @create: 2019-11-21 18:09
 **/
@Controller
public class MergeBranch implements IJGitPlugin {

    private Logger logger = LoggerFactory.getLogger(MergeBranch.class);

    @Override
    public String getId() {
        return "mergebranch";
    }

    @Override
    public JSONObject doService(JSONObject jsonObject) {
        JSONObject returnObj = new JSONObject();
        String targetBranch = jsonObject.optString("targetBra");
        String sourceBranch = jsonObject.optString("sourceBra");
        String repoName = jsonObject.optString("repoName");
        try {
            if (JGitUtil.paramBlankCheck(sourceBranch, targetBranch, repoName)){
                throw new ParamBlankException();
            }
             return BranchApi.branchMerge(JGitUtil.buildGitPath(repoName), sourceBranch, targetBranch, "succeed");
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
        jsonArray.add(new JGitHelpVo("repoName", "String", true, "repository name").parseJSON());
        jsonArray.add(new JGitHelpVo("targetBra", "String", true, "target repository name").parseJSON());
        jsonArray.add(new JGitHelpVo("sourceBra", "String", true, "source repository name").parseJSON());
        return jsonArray;
    }
}
