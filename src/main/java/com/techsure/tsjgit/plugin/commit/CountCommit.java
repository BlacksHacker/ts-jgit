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
import org.eclipse.jgit.revwalk.RevCommit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @program: ts-jgit
 * @description:
 * @create: 2019-10-24 17:56
 **/
@Component
public class CountCommit implements IJGitPlugin {

    Logger logger = LoggerFactory.getLogger(CountCommit.class);

    @Override
    public String getId() {
        return "countCommit";
    }

    @Override
    public Object doService(JSONObject jsonObject) {
        String repoName = jsonObject.optString("repoName");
        String revStr = jsonObject.optString("branchName");
        String path = jsonObject.optString("path");
        if (JGitUtil.paramBlankCheck(repoName)){
            throw new ParamBlankException();
        }
        int commitCount = 0;

        Iterable<RevCommit> commits;
        try {
            if (StringUtils.isBlank(revStr)){
                commits = CommitApi.listCommits(JGitUtil.buildGitPath(repoName));
            }else if (StringUtils.isBlank(path)){
                commits = CommitApi.listCommits(JGitUtil.buildGitPath(repoName), revStr);
            }else {
                commits = CommitApi.listCommits(JGitUtil.buildGitPath(repoName), revStr, path);
            }
            Iterator iterator = commits.iterator();
            while (iterator.hasNext()){
                commitCount ++;
                iterator.next();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return commitCount;
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
