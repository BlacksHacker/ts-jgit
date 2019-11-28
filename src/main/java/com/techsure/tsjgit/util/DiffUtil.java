package com.techsure.tsjgit.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.regex.Pattern;

/**
 * @program: ts-jgit
 * @description:
 * @create: 2019-11-28 16:08
 **/
public class DiffUtil {

    public static void main(String[] args) {

        JSONObject file = new JSONObject();
        JSONArray codeArray = new JSONArray();
        int add = 0;
        int del = 0;
        int keep = 0;
        String lineContent = "diff --git a/src/main/java/com/techsure/module/wuxibank/controller/FlowDashboardController.java b/src/main/java/com/techsure/module/wuxibank/controller/FlowDashboardController.java\n" +
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
                codeArray.add(lineObj);
                del++;
                continue;
            }

            if (lineCode.indexOf("+") == 0){
                JSONObject lineObj = new JSONObject();
                lineObj.put("type", "add");
                lineObj.put("code", lineCode.substring(1));
                codeArray.add(lineObj);
                add++;
                continue;
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

        }
        file.put("codeArray", codeArray);
        file.put("add", add);
        file.put("del", del);
        file.put("keep", keep);
        System.out.println(file);
    }
}
