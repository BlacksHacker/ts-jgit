package com.techsure.tsjgit.plugin.commit;

import com.techsure.tsjgit.api.CommitApi;
import com.techsure.tsjgit.dto.JGitCommitVo;
import com.techsure.tsjgit.dto.JGitHelpVo;
import com.techsure.tsjgit.exception.ParamBlankException;
import com.techsure.tsjgit.plugin.IJGitPlugin;
import com.techsure.tsjgit.util.JGitUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.util.StringUtil;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Iterator;

/**
 * @program: ts-jgit
 * @description:
 * @create: 2019-10-24 11:52
 **/
@Component
public class FirstCommit implements IJGitPlugin {

    Logger logger = LoggerFactory.getLogger(FirstCommit.class);

    @Override
    public String getId() {
        return "firstcommit";
    }

    @Override
    public JSONObject doService(JSONObject jsonObject) {
        JSONObject returnObj = new JSONObject();
        String repoName = jsonObject.optString("repoName");
        String revStr = jsonObject.optString("branchName");
        String path = jsonObject.optString("path");
        try {
            if (JGitUtil.paramBlankCheck(repoName)){
                throw new ParamBlankException();
            }

            Iterable<RevCommit> commits = CommitApi.listCommits(JGitUtil.buildGitPath(repoName), revStr, path);
            Iterator iterator = commits.iterator();
            if (iterator.hasNext()){
                returnObj.put("Status", "OK");
                returnObj.put("Data", new JGitCommitVo((RevCommit)iterator.next()));
            }
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
        jsonArray.add(new JGitHelpVo("branchName", "String", false, "branchName/tagName or HAS"));
        jsonArray.add(new JGitHelpVo("path", "String", false, " path after repository"));
        return jsonArray;
    }
}
