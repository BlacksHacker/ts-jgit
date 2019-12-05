package com.techsure.tsjgit.util;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @program: jgitservertest
 * @description:
 * @create: 2019-10-18 11:40
 **/
@Component
public class JGitUtil {

    public static String repositoryPath;

    @Value("${repository.root.path}")
    public void setRepositoryPath(String repositoryPath) {
        JGitUtil.repositoryPath = repositoryPath;
    }

    public static final String GIT_SUFFIX = ".git";
    public static final String REFS_HEAD = "refs/heads/";
    public static final String TAGS_HEAD = "refs/tags/";

    public static String timeParse(Date time){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(time);
    }

    public static boolean listCheck(List list){
        if (list != null && list.size() > 0){
            return true;
        }
        return false;
    }

    public static String buildGitPath(String repositoryName){
        StringBuilder gitPath = new StringBuilder();
        return gitPath.append(repositoryPath)
                    .append(File.separator)
                    .append(repositoryName)
                    .append(File.separator)
                    .append(GIT_SUFFIX)
                    .toString();
    }

    public static String buildFileFullPath(String repoName, String fileName, String path){
        StringBuilder filePath = new StringBuilder();
        filePath.append(repositoryPath)
                .append(File.separator)
                .append(repoName)
                .append(File.separator);
        if (StringUtils.isNotBlank(path)){
            filePath.append(path);
            filePath.append(File.separator);
        }
        return filePath.append(fileName)
                .toString();
    }

    public static String buildFileRelativePath(String fileName, String relativePath){
        return new StringBuilder()
                .append(relativePath)
                .append(File.separator)
                .append(fileName)
                .toString();
    }

    public static String buildFileFullPath(String repoName, String filName){
        return buildFileFullPath(repoName, filName, null);
    }

    public static String buildRepositoryPath(String repositoryName){
        StringBuilder gitPath = new StringBuilder();
        return gitPath.append(repositoryPath)
                .append(File.separator)
                .append(repositoryName)
                .toString();
    }

    public static String excludeRefHead(String RefName){
        return RefName.replace(REFS_HEAD,"");
    }

    public static String appendRefHead(String branchName){
        return REFS_HEAD + branchName;
    }

    public static String excludeTagHead(String tagName){
        return tagName.replace(TAGS_HEAD, "");
    }

    public static boolean paramBlankCheck(String...strings){
        for (String param : strings){
            if (StringUtils.isBlank(param)){
                return true;
            }
        }
        return false;
    }

    public static String toLinux(String filePath){
        return filePath.replace("\\", "/");
    }
}
