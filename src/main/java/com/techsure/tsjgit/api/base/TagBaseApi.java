package com.techsure.tsjgit.api.base;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;

import java.io.IOException;
import java.util.List;

/**
 * @program: jgitservertest
 * @description: git Tag底层封装
 * @create: 2019-10-23 09:57
 **/
public class TagBaseApi {

    private static final String TAG_HEAD_PATH = "refs/tags/";

    public static void tagCreate(Git git, String tagName) throws GitAPIException{
        git.tag().setName(tagName)
                .call();
    }

    public static void tagCreate(Git git, Repository repository, String tagName, String headHAS) throws IOException, GitAPIException{
        ObjectId id = repository.resolve(headHAS);
        try(RevWalk walk = new RevWalk(repository)){
            RevCommit commit = walk.parseCommit(id);
            git.tag().setObjectId(commit)
                    .setName(tagName)
                    .call();
        }
    }

    public static boolean tagExist(Git git, String tagName) throws GitAPIException{
        tagName = TAG_HEAD_PATH + tagName;
        List<Ref> refs = git.tagList().call();
        for (Ref ref : refs){
            if(tagName.equals(ref.getName())){
                return true;
            }
        }
        return false;
    }

    public static void tagDelete(Git git, String tagName) throws GitAPIException{
        git.tagDelete()
                .setTags(tagName)
                .call();
    }

    public static List<Ref> listTags(Git git) throws GitAPIException {
        return git.tagList().call();
    }

    public static Iterable<RevCommit> listTagCommits(Git git, Repository repository, Ref tagRef) throws IOException, GitAPIException{
        LogCommand log = git.log();
        Ref peeledRef = repository.getRefDatabase().peel(tagRef);
        if (peeledRef.getPeeledObjectId() != null){
            log.add(peeledRef.getPeeledObjectId());
        }else {
            log.add(tagRef.getObjectId());
        }
        return log.call();
    }
}
