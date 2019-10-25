package com.techsure.tsjgit.dto;

import java.util.List;

/**
 * @program: jgitservertest
 * @description:
 * @create: 2019-10-18 11:22
 **/
public class JGitFileVo {
    private String path;
    private List<JGitCommitVo> commitVos;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<JGitCommitVo> getCommitVos() {
        return commitVos;
    }

    public void setCommitVos(List<JGitCommitVo> commitVos) {
        this.commitVos = commitVos;
    }

    public JGitFileVo(String path, List<JGitCommitVo> commitVos) {
        this.path = path;
        this.commitVos = commitVos;
    }

    public JGitFileVo() {
    }

    @Override
    public String toString() {
        return "JGitFileVo{" +
                "path='" + path + '\'' +
                ", commitVos=" + commitVos +
                '}';
    }
}
