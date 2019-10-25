package com.techsure.tsjgit.plugin.branch;

import com.techsure.tsjgit.api.BranchApi;
import com.techsure.tsjgit.dto.JGitBranchVo;
import com.techsure.tsjgit.dto.JGitHelpVo;
import com.techsure.tsjgit.exception.ParamBlankException;
import com.techsure.tsjgit.plugin.IJGitPlugin;
import com.techsure.tsjgit.util.JGitUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.eclipse.jgit.lib.Ref;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: ts-jgit
 * @description:
 * @create: 2019-10-24 17:50
 **/
@Component
public class CountBranch implements IJGitPlugin {

    Logger logger = LoggerFactory.getLogger(CountBranch.class);

    @Override
    public String getId() {
        return "countBranch";
    }

    @Override
    public Object doService(JSONObject jsonObject) {
        String repoName = jsonObject.optString("repoName");
        if (JGitUtil.paramBlankCheck(repoName)){
            throw new ParamBlankException();
        }
        try {
            List<Ref> refs = BranchApi.listBranchs(JGitUtil.buildGitPath(repoName));
            if (JGitUtil.listCheck(refs)){
                return refs.size();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return 0;
    }

    @Override
    public JSONArray help() {
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(new JGitHelpVo("repoName", "String", true, "repository name").parseJSON());
        return jsonArray;
    }
}
