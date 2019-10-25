package com.techsure.tsjgit.dto;

import java.util.List;

/**
 * @program: jgitservertest
 * @description:
 * @create: 2019-10-21 11:34
 **/
public class JGitTagVo {
    private String tagName;
    private String createFrom;
    private String Message;
    private String releaseNote;
    private List<JGitCommitVo> commitVoList;

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getCreateFrom() {
        return createFrom;
    }

    public void setCreateFrom(String createFrom) {
        this.createFrom = createFrom;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getReleaseNote() {
        return releaseNote;
    }

    public void setReleaseNote(String releaseNote) {
        this.releaseNote = releaseNote;
    }

    public List<JGitCommitVo> getCommitVoList() {
        return commitVoList;
    }

    public void setCommitVoList(List<JGitCommitVo> commitVoList) {
        this.commitVoList = commitVoList;
    }
}
