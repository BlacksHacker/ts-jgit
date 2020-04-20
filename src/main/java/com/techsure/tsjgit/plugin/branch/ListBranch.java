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
        return "listbranch";
    }

    @Override
    public JSONObject doService(JSONObject jsonObject) {
        JSONObject returnObj = new JSONObject();
        String repoName = jsonObject.optString("repoName");
        List<JGitBranchVo> branchList = new ArrayList<>();
        try {
            /*if (JGitUtil.paramBlankCheck(repoName)){
                throw new ParamBlankException();
            }*/
            List<Ref> refs = BranchApi.listBranchs(JGitUtil.buildGitPath(repoName));
            for (Ref ref : refs){
                branchList.add(new JGitBranchVo(JGitUtil.excludeRefHead(ref.getName()), ref.getObjectId().getName()));
            }
            returnObj.put("Status", "OK");
            returnObj.put("Data", branchList);
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
