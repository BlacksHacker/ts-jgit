package com.techsure.tsjgit.plugin.file;

import com.techsure.tsjgit.api.FileApi;
import com.techsure.tsjgit.dto.JGitHelpVo;
import com.techsure.tsjgit.dto.JGitTagVo;
import com.techsure.tsjgit.exception.ParamBlankException;
import com.techsure.tsjgit.plugin.IJGitPlugin;
import com.techsure.tsjgit.util.JGitUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.techsure.tsjgit.util.JGitUtil.buildFileFullPath;

/**
 * @program: ts-jgit
 * @description:
 * @create: 2019-12-02 09:45
 **/
@Component
public class ShowContent implements IJGitPlugin {
    @Override
    public String getId() {
        return "showcontent";
    }

    @Override
    public JSONObject doService(JSONObject jsonObject) {
        String branchName = jsonObject.optString("braName");
        String lineNum = jsonObject.optString("lineNum");
        String repoName = jsonObject.optString("repoName");
        String filePath = jsonObject.optString("filePath");
        String sign = jsonObject.optString("sign");
        JSONObject returnObj = new JSONObject();
        try {
            if (JGitUtil.paramBlankCheck(branchName, repoName, filePath, lineNum, sign)){
                throw new ParamBlankException();
            }
            JSONArray fileContentArray = FileApi.fileContent(JGitUtil.buildGitPath(repoName), Integer.parseInt(lineNum), branchName, buildFileFullPath(repoName, filePath), sign);
            returnObj.put("Status", "OK");
            returnObj.put("fileContent", fileContentArray);
        } catch (Exception e) {
            returnObj.put("Status", "ERROR");
            returnObj.put("Message", e.getMessage());
        }
        return returnObj;
    }

    @Override
    public JSONArray help() {
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(new JGitHelpVo("repoName","String", true,"repository name").parseJSON());
        jsonArray.add(new JGitHelpVo("braName", "String", true, "branch name").parseJSON());
        jsonArray.add(new JGitHelpVo("lineNum", "String", true, "line num"));
        jsonArray.add(new JGitHelpVo("filePath", "String", true, "relative file Path"));
        return jsonArray;
    }
}
