package com.techsure.tsjgit.api;

import com.techsure.tsjgit.api.base.BranchBaseApi;
import com.techsure.tsjgit.api.base.RepositoryBaseApi;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.springframework.core.io.InputStreamSource;

import java.io.*;

/**
 * @program: ts-jgit
 * @description:
 * @create: 2019-12-02 09:46
 **/
public class FileApi {
    private static final String UP = "up";
    private static final String DOWN = "down";

    /** 
    * @Description: 获取文章指定行号内容 
    * @Param: [gitPath, lineNum, branchName, filePath, sign] 
    * @return: net.sf.json.JSONArray  
    */ 
    public static JSONArray fileContent(String gitPath, int lineNum, String branchName, String filePath, String sign) throws IOException, GitAPIException {
        JSONArray fileContentArray = new JSONArray();
        try(Repository repository = RepositoryBaseApi.openJGitRepository(gitPath)){
            try(Git git = new Git(repository)){
                BranchBaseApi.checkoutBranch(git, branchName);
                InputStream stream = new FileInputStream(filePath);
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                String lineContent = null;
                int lineCount = 0;

                while ((lineContent = reader.readLine()) != null){
                    lineCount++;
                    if (UP.equals(sign)){
                        if (lineCount < lineNum && lineCount >= (lineNum - 20)){
                            JSONObject lineObj = new JSONObject();
                            lineObj.put("code", lineContent);
                            lineObj.put("lineNum", lineCount);
                            fileContentArray.add(lineObj);
                        }
                        if (lineCount >= lineNum){
                            break;
                        }
                    }

                    if (DOWN.equals(sign)){
                        if (lineCount > lineNum){
                            JSONObject lineObj = new JSONObject();
                            lineObj.put("code", lineContent);
                            lineObj.put("lineNum", lineCount);
                            fileContentArray.add(lineObj);
                        }
                        if (lineCount >= (lineNum + 20)){
                            break;
                        }
                    }
                }
            }
        }
        return fileContentArray;
    }
}
