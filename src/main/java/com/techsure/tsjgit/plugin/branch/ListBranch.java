package com.techsure.tsjgit.plugin.branch;

import com.techsure.tsjgit.api.BranchApi;
import com.techsure.tsjgit.dto.JGitBranchVo;
import com.techsure.tsjgit.dto.JGitHelpVo;
import com.techsure.tsjgit.exception.ParamBlankException;
import com.techsure.tsjgit.plugin.IJGitPlugin;
import com.techsure.tsjgit.util.JGitUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jgit.lib.Ref;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: ts-jgit
 * @description:
 * @create: 2019-10-24 12:11
 **/
@Component
public class ListBranch implements IJGitPlugin {

    Logger logger = LoggerFactory.getLogger(ListBranch.class);

    @Override
    public String getId() {
        return "listBranch";
    }

    @Override
    public Object doService(JSONObject jsonObject) {
        String repoName = jsonObject.optString("repoName");
        if (JGitUtil.paramBlankCheck(repoName)){
            throw new ParamBlankException();
        }
        List<JGitBranchVo> branchList = new ArrayList<>();
        try {
            List<Ref> refs = BranchApi.listBranchs(JGitUtil.buildGitPath(repoName));
            for (Ref ref : refs){
                branchList.add(new JGitBranchVo(JGitUtil.excludeRefHead(ref.getName()), ref.getObjectId().getName()));
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return branchList;
    }

    @Override
    public JSONArray help() {
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(new JGitHelpVo("repoName", "String", true, "repository name").parseJSON());
        return jsonArray;
    }
}
