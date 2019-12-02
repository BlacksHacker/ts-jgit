package com.techsure.tsjgit.util;
;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.logging.log4j.util.StringBuilderFormattable;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @program: ts-jgit
 * @description:
 * @create: 2019-11-28 16:08
 **/
public class DiffUtil {

    public static JSONObject getDiffCodeJson(String lineContent){
        JSONObject file = new JSONObject();
        JSONArray codeArray = new JSONArray();
        JSONArray delArray = new JSONArray();
        JSONArray addArray = new JSONArray();
        int add = 0;
        int del = 0;
        int keep = 0;
    /*    String lineContent = "diff --git a/src/main/java/com/techsure/module/wuxibank/controller/FlowDashboardController.java b/src/main/java/com/techsure/module/wuxibank/controller/FlowDashboardController.java\n" +
                "index a2f8f56..b5b4633 100644\n" +
                "--- a/src/main/java/com/techsure/module/wuxibank/controller/FlowDashboardController.java\n" +
                "+++ b/src/main/java/com/techsure/module/wuxibank/controller/FlowDashboardController.java\n" +
                "@@ -63,7 +63,7 @@\n" +
                "                 }\n" +
                "             }\n" +
                "         }\n" +
                "-        System.out.println(jsonArray);\n" +
                "+\n" +
                "         return jsonArray;\n" +
                "     }\n" +
                "     \n";
*/
        String[] lineArray = lineContent.split("\n");

        for (int i = 0; i < lineArray.length; i++){
            String lineCode = lineArray[i];

            String pattern_1 = "^diff --git \'?(.+)\'? \'?(.+)\'?";
            if (Pattern.matches(pattern_1, lineCode)){
                String[] lineData = lineCode.split(" ");
                file.put("fromName", lineData[2]);
                file.put("toName", lineData[3]);
                continue;
            }

            if (lineCode.indexOf("--- ") == 0){
                if (!file.has("oldName")){
                    file.put("oldName", lineCode.substring(4));
                }
                continue;
            }

            if (lineCode.indexOf("+++ ") == 0){
                if (!file.has("newName")){
                    file.put("newName", lineCode.substring(4));
                }
                continue;
            }

            if (lineCode.indexOf("-") == 0){
                JSONObject lineObj = new JSONObject();
                lineObj.put("type", "del");
                lineObj.put("code", lineCode.substring(1));
                delArray.add(lineObj);
                del++;
            }

            if (lineCode.indexOf("+") == 0){
                JSONObject lineObj = new JSONObject();
                lineObj.put("type", "add");
                lineObj.put("code", lineCode.substring(1));
                addArray.add(lineObj);
                add++;
            }

            if (lineCode.indexOf(" ") == 0){
                JSONObject lineObj = new JSONObject();
                lineObj.put("type", "keep");
                lineObj.put("code", lineCode.substring(1));
                codeArray.add(lineObj);
                keep++;
                continue;
            }

            String pattern_2 = "^index ([0-9a-z]+)\\.\\.([0-9a-z]+)\\s*(\\d{6})?";
            if (Pattern.matches(pattern_2, lineCode)){
                file.put("before", lineCode.split(" ")[1].split("\\.\\.")[0]);
                file.put("after", lineCode.split(" ")[1].split("\\.\\.")[1]);
                file.put("mode", lineCode.split(" ")[2]);
            }

            if (addArray.size() != 0 && (addArray.size() == delArray.size())){
                pickDiffCode(delArray, addArray, codeArray);
            }
        }
        file.put("codeArray", codeArray);
        file.put("add", add);
        file.put("del", del);
        file.put("keep", keep);
        return file;
    }
    public static void pickDiffCode(JSONArray addArray, JSONArray delArray, JSONArray codeArray){
        for (int i = 0; i < addArray.size(); i++){
            JSONObject delObj = delArray.getJSONObject(i);
            JSONObject addObj = addArray.getJSONObject(i);
            DiffMatchPatch diffMatchPatch = new DiffMatchPatch();
            List<DiffMatchPatch.Diff> diffList = diffMatchPatch.diff_main(delObj.optString("code"), addObj.optString("code"));
            if (JGitUtil.listCheck(diffList)){
                int delCount = 0;
                StringBuffer delBuffer = new StringBuffer();
                int addCount = 0;
                StringBuffer addBuffer = new StringBuffer();
                for (DiffMatchPatch.Diff diff : diffList){
                    if (diff.operation.equals(DiffMatchPatch.Operation.DELETE)){
                        delCount ++;
                        delBuffer.setLength(0);
                        delBuffer.append(diff.text);
                    }
                    if (diff.operation.equals(DiffMatchPatch.Operation.INSERT)){
                        addCount++;
                        addBuffer.setLength(0);
                        addBuffer.append(diff.text);
                    }
                }
                if (addCount == 0 && delCount == 1) {
                    delObj.put("diff", delBuffer.toString());
                }

                if(addCount == 1 && delCount == 0){
                    addObj.put("diff", addBuffer.toString());
                }
            }
        }
        codeArray.addAll(delArray);
        codeArray.addAll(addArray);
    }
}
