package com.techsure.tsjgit.api.base;

import com.techsure.tsjgit.util.JGitUtil;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jgit.api.DiffCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.MergeCommand;
import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.filter.PathFilter;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @program: jgitservertest
 * @description: git Branch底层封装
 * @create: 2019-10-23 09:54
 **/
public class BranchBaseApi {

    private static final String REFS_HEAD = "refs/heads/";

    public static void branchCreate(Git git, String branchName) throws IOException, GitAPIException{
        git.branchCreate()
                .setName(branchName)
                .call();
    }

    public static void branchCreate(Git git, String branchName, String startPoint) throws IOException, GitAPIException{
        git.branchCreate()
                .setName(branchName)
                .setStartPoint(startPoint)
                .call();
    }

    public static void branchDelete(Git git, String branchName) throws IOException, GitAPIException{
        git.branchDelete()
                .setBranchNames(branchName)
                .setForce(true)
                .call();
    }

    public static boolean branchExist(Git git, String branchName) throws IOException, GitAPIException{
        branchName = REFS_HEAD + branchName;
        List<Ref> refs = git.branchList().call();
        for (Ref ref : refs){
            if (branchName.equals(ref.getName())){
                return true;
            }
        }
        return false;
    }

    public static MergeResult branchMerge(Git git, Repository repository, String sourceBra, String targetBra) throws IOException, GitAPIException{
        ObjectId mergeBase = repository.resolve(sourceBra);
        BranchBaseApi.checkoutBranch(git, targetBra);
        MergeResult result = git.merge()
                .include(mergeBase)
                .setCommit(false)
                .setFastForward(MergeCommand.FastForwardMode.NO_FF)
                .call();
        if (result.getConflicts() == null){

        }
        return result;
    }

    public static List<Ref> listBranches(Git git) throws GitAPIException {
        return git.branchList().call();
    }

    public static List<DiffEntry> diffBranches(Git git, String sourceBra, String targetBra, Repository repository, String fileName) throws IOException, GitAPIException{
        AbstractTreeIterator oldTreeParser = prepareTreeParser(repository, JGitUtil.appendRefHead(targetBra));
        AbstractTreeIterator newTreeParser = prepareTreeParser(repository, JGitUtil.appendRefHead(sourceBra));
        DiffCommand command = git.diff()
                .setOldTree(oldTreeParser)
                .setNewTree(newTreeParser);
        if (StringUtils.isNotBlank(fileName)){
            command.setPathFilter(PathFilter.create(fileName));
        }
        return command.call();
    }

    private static AbstractTreeIterator prepareTreeParser(Repository repository, String ref) throws IOException {
        Ref head = repository.exactRef(ref);
        try(RevWalk walk = new RevWalk(repository)){
            RevCommit commit = walk.parseCommit(head.getObjectId());
            RevTree tree = walk.parseTree(commit.getTree().getId());
            CanonicalTreeParser treeParser = new CanonicalTreeParser();
            try(ObjectReader reader = repository.newObjectReader()){
                treeParser.reset(reader, tree.getId());
            }
            walk.dispose();
            return treeParser;
        }
    }

    public static void checkoutBranch(Git git, String branchName) throws GitAPIException{
        String name = JGitUtil.appendRefHead(branchName);
        git.checkout().setName(name).call();
    }

    public static void checkoutListPaths(Git git, List<String> paths) throws GitAPIException{
        git.checkout().addPaths(paths).setForced(true).call();
    }
}
