package com.techsure.tsjgit.plugin.file;

import com.techsure.tsjgit.api.BranchApi;
import com.techsure.tsjgit.api.RepositoryApi;
import com.techsure.tsjgit.dto.JGitHelpVo;
import com.techsure.tsjgit.exception.ParamBlankException;
import com.techsure.tsjgit.plugin.IJGitPlugin;
import com.techsure.tsjgit.util.JGitUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @program: ts-jgit
 * @description:
 * @create: 2019-12-05 10:01
 **/
@Component
public class AddCatalog implements IJGitPlugin {
    Logger logger = LoggerFactory.getLogger(AddCatalog.class);

    @Override
    public String getId() {
        return "addCatalog";
    }

    @Override
    public JSONObject doService(JSONObject jsonObject) {
        JSONObject returnObj = new JSONObject();
        String repoName = jsonObject.optString("repoName");
        String braName = jsonObject.optString("braName");
        String catalog = jsonObject.optString("catalog");
        String path = jsonObject.optString("path");
        boolean isAuth = jsonObject.optBoolean("isAuth");
        String newBraName = jsonObject.optString("newBraName");
        String message = jsonObject.optString("message");
        try {
            if (JGitUtil.paramBlankCheck(repoName, braName, catalog, message, path)){
                throw new ParamBlankException();
            }
            String fullPath = JGitUtil.buildFileFullPath(repoName, catalog, path);
            String relativePath = JGitUtil.buildFileRelativePath(catalog, path);
            File file = new File(fullPath);
            String gitPath = JGitUtil.buildGitPath(repoName);
            BranchApi.checkoutBranch(gitPath, braName);
            if (file.exists()){
                throw new FileExistsException();
            }
            //是否有权限直接提交
            if (!isAuth){
                if (JGitUtil.paramBlankCheck(newBraName)){
                    throw new ParamBlankException();
                }
                BranchApi.branchCreate(gitPath, newBraName, braName);
                BranchApi.checkoutBranch(gitPath, newBraName);
            }
            file.createNewFile();
            RepositoryApi.commitFile(gitPath, message, JGitUtil.toLinux(relativePath));
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
        jsonArray.add(new JGitHelpVo("braName", "String", true, "branchName of from").parseJSON());
        jsonArray.add(new JGitHelpVo("newBraName", "String", false, "new branchName").parseJSON());
        jsonArray.add(new JGitHelpVo("message", "String", true, "commit message").parseJSON());
        jsonArray.add(new JGitHelpVo("catalog", "String", true, "catalog name").parseJSON());
        jsonArray.add(new JGitHelpVo("path", "String", true, "belong path").parseJSON());
        jsonArray.add(new JGitHelpVo("isAuth", "Boolean", true, "have auth").parseJSON());
        return jsonArray;
    }
}
