package com.techsure.tsjgit.plugin.tag;

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
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
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
 * @create: 2019-10-24 12:05
 **/
@Component
public class ListTag implements IJGitPlugin {
    Logger logger = LoggerFactory.getLogger(ListTag.class);

    @Override
    public String getId() {
        return "listTag";
    }

    @Override
    public Object doService(JSONObject jsonObject) {

        String repoName = jsonObject.optString("repoName");
        if (JGitUtil.paramBlankCheck(repoName)){
            throw new ParamBlankException();
        }
        List<JGitTagVo> tagList = new ArrayList<>();
        try(Repository repository = RepositoryBaseApi.openJGitRepository(JGitUtil.buildGitPath(repoName))){
            try(Git git = new Git(repository)) {
                List<Ref> refs = TagBaseApi.listTags(git);
                for (Ref ref : refs){
                    JGitTagVo tagVo = new JGitTagVo();
                    List<JGitCommitVo> commitList = new ArrayList<>();
                    tagVo.setTagName(JGitUtil.excludeTagHead(ref.getName()));
                    Iterable<RevCommit> commits = TagBaseApi.listTagCommits(git, repository, ref);
                    Iterator iterator = commits.iterator();
                    while (iterator.hasNext()){
                        RevCommit commit = (RevCommit) iterator.next();
                        commitList.add(new JGitCommitVo(commit));
                    }
                    tagVo.setCommitVoList(commitList);
                    tagList.add(tagVo);
                }
            }
        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }
        return tagList;
    }

    @Override
    public JSONArray help() {
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(new JGitHelpVo("repoName", "String", true, "repository name").parseJSON());
        return jsonArray;
    }
}
