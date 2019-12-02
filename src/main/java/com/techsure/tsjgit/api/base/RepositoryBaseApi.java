package com.techsure.tsjgit.api.base;

import com.techsure.tsjgit.util.JGitUtil;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;

/**
 * @program: jgitservertest
 * @description: git Commit底层封装
 * @create: 2019-10-23 10:02
 **/
public class RepositoryBaseApi {

    public static Repository openJGitRepository(String gitPath) throws IOException {
        File gitFile = new File(gitPath);
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        return builder.setGitDir(gitFile).build();
    }

    public static void initRepository(File dir) throws GitAPIException { ;
        Git.init().setDirectory(dir).call();
    }

    public static void commitFile(Git git, String message, String fileFullPath) throws GitAPIException{
        git.add()
                .addFilepattern(fileFullPath)
                .call();
        git.commit()
                .setMessage(message)
                .call();
    }
}
