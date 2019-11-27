package com.techsure.tsjgit.dispatcher;

import com.techsure.tsjgit.Application;
import com.techsure.tsjgit.common.Config;
import com.techsure.tsjgit.exception.ServiceNotFindException;
import com.techsure.tsjgit.plugin.IJGitPlugin;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: ts-jgit
 * @description:
 * @create: 2019-10-23 16:53
 **/
@RestController()
@RequestMapping("/rest/")
public class JGitRestDispatcher {

    private static final String SERVICE = "service";

    private static final String HELP = "help";

    Logger logger = LoggerFactory.getLogger(JGitRestDispatcher.class);

    private void doIt(HttpServletRequest request, String token, JSONObject paramJson, JSONObject jsonObj, String action) throws Exception {
        /*Authenticator.authenticate(request);*/
        if (true) {
            IJGitPlugin plugin = Application.getPlugin(token);
            if (plugin != null) {
                if (action.equals(SERVICE)) {
                    jsonObj.put("Return", plugin.doService(paramJson));
                } else {
                    jsonObj.put("Return", plugin.help());
                }
            } else {
                throw new ServiceNotFindException();
            }

        }
    }

    @RequestMapping(value = "/{token}/{action}", method = RequestMethod.POST)
    public void dispatcherForPost(@PathVariable String token, @PathVariable String action, @RequestBody JSONObject json, HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSONObject jsonObj = new JSONObject();
        try {
            doIt(request, token, json, jsonObj, action);
        }catch (Exception ex) {
            jsonObj.put("Error", 500);
            jsonObj.put("Status", "ERROR");
            jsonObj.put("Message", ex.getMessage());
            logger.error(ex.getMessage(), ex);
        }
        response.setContentType(Config.RESPONSE_TYPE_JSON);
        response.getWriter().print(jsonObj);
    }
}
