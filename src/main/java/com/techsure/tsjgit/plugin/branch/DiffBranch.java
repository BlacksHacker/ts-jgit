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
import org.springframework.stereotype.Component;


/**
 * @program: ts-jgit
 * @description:
 * @create: 2019-11-25 18:27
 **/
@Component
public class DiffBranch implements IJGitPlugin {
    Logger logger = LoggerFactory.getLogger(DiffBranch.class);

    @Override
    public String getId() {
        return "diffbranch";
    }

    @Override
    public JSONObject doService(JSONObject jsonObject) {
        JSONObject returnObj = new JSONObject();
        String target = jsonObject.optString("targetBra");
        String source = jsonObject.optString("sourceBra");
        String repoName = jsonObject.optString("repoName");
        String fileName = jsonObject.optString("fileName");
        try {
            if (JGitUtil.paramBlankCheck(source, target, repoName)){
                throw new ParamBlankException();
            }
            JSONObject diffData = BranchApi.diffBranch(JGitUtil.buildGitPath(repoName), source, target, fileName);
            returnObj.put("Status", "OK");
            returnObj.put("Data", diffData);
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
        jsonArray.add(new JGitHelpVo("fileName", "String", false, "specific file full path ").parseJSON());
        return jsonArray;
    }
}
