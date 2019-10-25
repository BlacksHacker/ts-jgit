package com.techsure.tsjgit.dto;

import net.sf.json.JSONObject;

/**
 * @program: ts-jgit
 * @description:
 * @create: 2019-10-24 09:51
 **/
public class JGitHelpVo {
    private String param;
    private String type;
    private boolean required;
    private String desc;

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public JSONObject parseJSON(){
        JSONObject obj = new JSONObject();
        obj.put("param", param);
        obj.put("type", type);
        obj.put("required", required);
        obj.put("desc", desc);
        return obj;
    }

    public JGitHelpVo(String param, String type, boolean required, String desc) {
        this.param = param;
        this.type = type;
        this.required = required;
        this.desc = desc;
    }

    public JGitHelpVo() {
    }
}
