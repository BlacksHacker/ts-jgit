package com.techsure.tsjgit.plugin.tag;

import com.techsure.tsjgit.api.TagApi;
import com.techsure.tsjgit.dto.JGitHelpVo;
import com.techsure.tsjgit.exception.ParamBlankException;
import com.techsure.tsjgit.plugin.IJGitPlugin;
import com.techsure.tsjgit.util.JGitUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * @program: ts-jgit
 * @description:
 * @create: 2019-12-04 10:42
 **/
@Component
public class CheckTagExist implements IJGitPlugin {
    Logger logger = LoggerFactory.getLogger(CheckTagExist.class);

    @Override
    public String getId() {
        return "checktagexist";
    }

    @Override
    public JSONObject doService(JSONObject jsonObject) {
        String repoName = jsonObject.optString("repoName");
        String tagName = jsonObject.optString("tagName");
        JSONObject returnObj = new JSONObject();
        boolean isExist = false;
        try {
            if (JGitUtil.paramBlankCheck(repoName, tagName)){
                throw new ParamBlankException();
            }
            List<Ref> refs = TagApi.listTags(JGitUtil.buildGitPath(repoName));
            for (Ref ref : refs){
                if (tagName.equals(JGitUtil.excludeTagHead(ref.getName()))){
                    isExist = true;
                    break;
                }
            }
            returnObj.put("Status", "OK");
            returnObj.put("Data", isExist);
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
        jsonArray.add(new JGitHelpVo("tagName", "String", true, "tag name").parseJSON());
        return jsonArray;
    }
}
