package com.techsure.tsjgit.dto;

import java.util.List;

/**
 * @program: jgitservertest
 * @description:
 * @create: 2019-10-18 16:21
 **/
public class JGitBranchVo {
    private String branchName;
    private String branchHAS;

    private String createFrom;

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getCreateFrom() {
        return createFrom;
    }

    public void setCreateFrom(String createFrom) {
        this.createFrom = createFrom;
    }

    public String getBranchHAS() {
        return branchHAS;
    }

    public void setBranchHAS(String branchHAS) {
        this.branchHAS = branchHAS;
    }

    public JGitBranchVo(String branchName, String branchHAS) {
        this.branchName = branchName;
        this.branchHAS = branchHAS;
    }
}
