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
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @program: ts-jgit
 * @description: 获取仓库commit集合
 * @create: 2019-10-23 18:33
 **/
@Component
public class ListCommit implements IJGitPlugin {

    Logger logger = LoggerFactory.getLogger(ListCommit.class);

    @Override
    public String getId() {
        return "listcommit";
    }

    @Override
    public JSONObject doService(JSONObject jsonObject) {
        JSONObject returnObj = new JSONObject();
        String repoName = jsonObject.optString("repoName");
        String braName = jsonObject.optString("braName");
        String excludeBraName = jsonObject.optString("excludeBraName");
        String path = jsonObject.optString("path");

        List<JGitCommitVo> commitList = new ArrayList<>();
        try {
            if (JGitUtil.paramBlankCheck(repoName)){
                throw new ParamBlankException();
            }
            Iterable<RevCommit> commits = CommitApi.listCommits(JGitUtil.buildGitPath(repoName), braName, excludeBraName, path);
            Iterator iterator = commits.iterator();
            while (iterator.hasNext()){
                RevCommit commit = (RevCommit)iterator.next();
                commitList.add(new JGitCommitVo(commit));
            }
            returnObj.put("Status", "OK");
            returnObj.put("Data", commitList);
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
        jsonArray.add(new JGitHelpVo("braName", "String", false, "branchName/tagName or HAS"));
        jsonArray.add(new JGitHelpVo("excludeBraName", "String", false, "exclude branchName/tagName or HAS"));
        jsonArray.add(new JGitHelpVo("path", "String", false, " path after repository"));
        return jsonArray;
    }
}
