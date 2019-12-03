package com.techsure.tsjgit.plugin.common;

import com.techsure.tsjgit.api.BranchApi;
import com.techsure.tsjgit.api.CommonApi;
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
public class DiffPoint implements IJGitPlugin {
    Logger logger = LoggerFactory.getLogger(DiffPoint.class);

    @Override
    public String getId() {
        return "diffpoint";
    }

    @Override
    public JSONObject doService(JSONObject jsonObject) {
        JSONObject returnObj = new JSONObject();
        String sourceHAS = jsonObject.optString("sourceHAS");
        String targetHAS = jsonObject.optString("targetHAS");
        String repoName = jsonObject.optString("repoName");
        String fileName = jsonObject.optString("fileName");
        try {
            if (JGitUtil.paramBlankCheck(sourceHAS, targetHAS, repoName)){
                throw new ParamBlankException();
            }
            JSONArray diffData = CommonApi.diffPoint(JGitUtil.buildGitPath(repoName), sourceHAS, targetHAS, fileName);
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
        jsonArray.add(new JGitHelpVo("targetHAS", "String", true, "target HAS").parseJSON());
        jsonArray.add(new JGitHelpVo("sourceHAS", "String", true, "source HAS").parseJSON());
        jsonArray.add(new JGitHelpVo("fileName", "String", false, "specific file relative path ").parseJSON());
        return jsonArray;
    }
}
