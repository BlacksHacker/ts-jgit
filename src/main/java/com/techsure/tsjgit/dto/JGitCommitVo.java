package com.techsure.tsjgit.dto;

import com.techsure.tsjgit.util.JGitUtil;
import javafx.scene.input.DataFormat;
import org.eclipse.jgit.revwalk.RevCommit;

import java.text.SimpleDateFormat;

/**
 * @program: jgitservertest
 * @description:
 * @create: 2019-10-18 11:24
 **/
public class JGitCommitVo {
    private String fullMessage;
    private String shoutMessage;
    private String commitTime;
    private String authorName;
    private String authorEmailAddress;
    private String commitSHA;

    public String getFullMessage() {
        return fullMessage;
    }

    public void setFullMessage(String fullMessage) {
        this.fullMessage = fullMessage;
    }

    public String getShoutMessage() {
        return shoutMessage;
    }

    public void setShoutMessage(String shoutMessage) {
        this.shoutMessage = shoutMessage;
    }

    public String getCommitTime() {
        return commitTime;
    }

    public void setCommitTime(String commitTime) {
        this.commitTime = commitTime;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorEmailAddress() {
        return authorEmailAddress;
    }

    public void setAuthorEmailAddress(String authorEmailAddress) {
        this.authorEmailAddress = authorEmailAddress;
    }

    public String getCommitSHA() {
        return commitSHA;
    }

    public void setCommitSHA(String commitSHA) {
        this.commitSHA = commitSHA;
    }

    @Override
    public String toString() {
        return "JGitCommitVo{" +
                "fullMessage='" + fullMessage + '\'' +
                ", shoutMessage='" + shoutMessage + '\'' +
                ", commitTime='" + commitTime + '\'' +
                ", authorName='" + authorName + '\'' +
                ", authorEmailAddress='" + authorEmailAddress + '\'' +
                ", commitSHA='" + commitSHA + '\'' +
                '}';
    }

    public JGitCommitVo() {
    }

    public JGitCommitVo(RevCommit revCommit){

        this.fullMessage = revCommit.getFullMessage();
        this.shoutMessage = revCommit.getShortMessage();
        this.commitSHA = revCommit.getId().getName();
        this.authorName = revCommit.getAuthorIdent().getName();
        this.authorEmailAddress = revCommit.getAuthorIdent().getEmailAddress();
        this.commitTime = JGitUtil.timeParse(revCommit.getAuthorIdent().getWhen());
    }
}
