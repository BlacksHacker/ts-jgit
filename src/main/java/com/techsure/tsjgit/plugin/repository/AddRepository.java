package com.techsure.tsjgit.plugin.repository;

import com.techsure.tsjgit.api.base.RepositoryBaseApi;
import com.techsure.tsjgit.dto.JGitHelpVo;
import com.techsure.tsjgit.exception.ParamBlankException;
import com.techsure.tsjgit.exception.RepositoryExistException;
import com.techsure.tsjgit.plugin.IJGitPlugin;
import com.techsure.tsjgit.util.JGitUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.io.File;

/**
 * @program: ts-jgit
 * @description:
 * @create: 2019-10-25 12:03
 **/
@Component
public class AddRepository implements IJGitPlugin {

    Logger logger = LoggerFactory.getLogger(AddRepository.class);

    @Override
    public String getId() {
        return "addrepository";
    }

    @Override
    public JSONObject doService(JSONObject jsonObject) {
        JSONObject returnObj = new JSONObject();
        String repoName = jsonObject.optString("repoName");
        try {
            if (JGitUtil.paramBlankCheck(repoName)){
                throw new ParamBlankException();
            }
            File dir = new File(JGitUtil.buildRepositoryPath(repoName));
            if (dir.exists()){
                throw new RepositoryExistException();
            }
            dir.mkdir();
            RepositoryBaseApi.initRepository(dir);
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
        return jsonArray;
    }
}
