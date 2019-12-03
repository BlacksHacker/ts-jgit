package com.techsure.tsjgit.api;

import com.techsure.tsjgit.api.base.BranchBaseApi;
import com.techsure.tsjgit.api.base.CommonBaseApi;
import com.techsure.tsjgit.api.base.RepositoryBaseApi;
import com.techsure.tsjgit.util.DiffUtil;
import net.sf.json.JSONArray;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.lib.Repository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @program: ts-jgit
 * @description:
 * @create: 2019-12-03 18:35
 **/
public class CommonApi {

    /**
     * @Description: 分支文件差异
     * @Param: [gitPath, sourceBra, targetBra, fileName]
     * @return: net.sf.json.JSONObject
     */
    public static JSONArray diffPoint(String gitPath, String sourceHAS, String targetHAS, String fileName) throws IOException, GitAPIException {
        JSONArray diffArray = new JSONArray();
        try(Repository repository = RepositoryBaseApi.openJGitRepository(gitPath)){
            try(Git git = new Git(repository)){
                List<DiffEntry> diff = CommonBaseApi.diffPoint(git, sourceHAS, targetHAS, repository, fileName);
                try(ByteArrayOutputStream stream = new ByteArrayOutputStream()){
                    try (DiffFormatter formatter = new DiffFormatter(stream)) {
                        formatter.setRepository(repository);
                        for (DiffEntry entry : diff) {
                            formatter.format(entry);
                            diffArray.add(DiffUtil.getDiffCodeJson(stream.toString()));
                            stream.flush();
                        }
                    }
                }
            }
        }
        return diffArray;
    }
}
