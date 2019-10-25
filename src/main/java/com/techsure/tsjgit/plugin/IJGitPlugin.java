package com.techsure.tsjgit.plugin;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public interface IJGitPlugin {

    public String getId();

    public Object doService( JSONObject jsonObject);

    public JSONArray help();
}
