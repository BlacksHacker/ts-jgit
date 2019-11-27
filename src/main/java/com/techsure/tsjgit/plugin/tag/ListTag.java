package com.techsure.tsjgit.plugin.tag;

import com.techsure.tsjgit.api.TagApi;
import com.techsure.tsjgit.api.base.RepositoryBaseApi;
import com.techsure.tsjgit.api.base.TagBaseApi;
import com.techsure.tsjgit.dto.JGitCommitVo;
import com.techsure.tsjgit.dto.JGitHelpVo;
import com.techsure.tsjgit.dto.JGitTagVo;
import com.techsure.tsjgit.exception.ParamBlankException;
import com.techsure.tsjgit.plugin.IJGitPlugin;
import com.techsure.tsjgit.util.JGitUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
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
 * @description:
 * @create: 2019-10-24 12:05
 **/
@Component
public class ListTag implements IJGitPlugin {
    Logger logger = LoggerFactory.getLogger(ListTag.class);

    @Override
    public String getId() {
        return "listtag";
    }

    @Override
    public JSONObject doService(JSONObject jsonObject) {
        JSONObject returnObj = new JSONObject();
        String repoName = jsonObject.optString("repoName");
        List<JGitTagVo> tagList = new ArrayList<>();
        try {
            if (JGitUtil.paramBlankCheck(repoName)){
                throw new ParamBlankException();
            }
            String gitPath = JGitUtil.buildGitPath(repoName);
            List<Ref> refs = TagApi.listTags(JGitUtil.buildGitPath(gitPath));
            for (Ref ref : refs){
                Iterable<RevCommit> commits = TagApi.listTagCommits(gitPath, ref);
                Iterator iterator = commits.iterator();
                JGitTagVo tagVo = new JGitTagVo();
                List<JGitCommitVo> commitList = new ArrayList<>();
                tagVo.setTagName(JGitUtil.excludeTagHead(ref.getName()));
                while (iterator.hasNext()){
                    RevCommit commit = (RevCommit) iterator.next();
                    commitList.add(new JGitCommitVo(commit));
                }
                tagVo.setCommitVoList(commitList);
                tagList.add(tagVo);
            }
            returnObj.put("Status", "OK");
            returnObj.put("Data", tagList);
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
        return jsonArray;
    }
}
