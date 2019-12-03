package com.techsure.tsjgit.api.base;

import com.techsure.tsjgit.util.JGitUtil;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jgit.api.DiffCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.filter.PathFilter;

import java.io.IOException;
import java.util.List;

/**
 * @program: ts-jgit
 * @description:
 * @create: 2019-12-03 18:37
 **/
public class CommonBaseApi {

    public static List<DiffEntry> diffPoint(Git git, String sourceBra, String targetBra, Repository repository, String fileName) throws IOException, GitAPIException {
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

    private static AbstractTreeIterator prepareTreeParser(Repository repository, String ObjectId) throws IOException {
        try(RevWalk walk = new RevWalk(repository)){
            RevCommit commit = walk.parseCommit(repository.resolve(ObjectId));
            RevTree tree = walk.parseTree(commit.getTree().getId());
            CanonicalTreeParser treeParser = new CanonicalTreeParser();
            try(ObjectReader reader = repository.newObjectReader()){
                treeParser.reset(reader, tree.getId());
            }
            walk.dispose();
            return treeParser;
        }
    }
}
