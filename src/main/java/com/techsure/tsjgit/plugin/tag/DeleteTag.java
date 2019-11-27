package com.techsure.tsjgit.plugin.tag;

import com.techsure.tsjgit.api.TagApi;
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
 * @create: 2019-10-24 16:06
 **/
@Component
public class DeleteTag implements IJGitPlugin {
    Logger logger = LoggerFactory.getLogger(DeleteTag.class);

    @Override
    public String getId() {
        return "deletetag";
    }

    @Override
    public JSONObject doService(JSONObject jsonObject) {
        JSONObject returnObj = new JSONObject();
        String repoName = jsonObject.optString("repoName");
        String tagName = jsonObject.optString("tagName");
        try {
            if (JGitUtil.paramBlankCheck(repoName, tagName)){
                throw new ParamBlankException();
            }
            TagApi.tagDelete(JGitUtil.buildGitPath(repoName), tagName);
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
        jsonArray.add(new JGitHelpVo("tagName", "String", true, "new Tag name"));
        return jsonArray;
    }
}
