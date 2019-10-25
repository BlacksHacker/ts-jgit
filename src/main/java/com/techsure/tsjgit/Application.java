package com.techsure.tsjgit;

import com.techsure.tsjgit.plugin.IJGitPlugin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: ts-jgit
 * @description:
 * @create: 2019-10-23 16:47
 **/
@SpringBootApplication
public class Application {

    private static Map<String, IJGitPlugin> pluginMap = new HashMap<>();

    public static IJGitPlugin getPlugin(String id){
        return pluginMap.get(id);
    }

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Application.class, args);
        Map<String, IJGitPlugin> myMap = context.getBeansOfType(IJGitPlugin.class);
        for (Map.Entry<String, IJGitPlugin> entry : myMap.entrySet()){
            IJGitPlugin plugin = entry.getValue();
            if (plugin.getId() != null){
                pluginMap.put(plugin.getId(), plugin);
            }
        }
    }

}
