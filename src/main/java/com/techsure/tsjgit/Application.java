package com.techsure.tsjgit;

import com.techsure.tsjgit.plugin.IJGitPlugin;
import com.techsure.tsjgit.util.JGitUtil;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.eclipse.jgit.http.server.GitServlet;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.ServiceMayNotContinueException;
import org.eclipse.jgit.transport.resolver.RepositoryResolver;
import org.eclipse.jgit.transport.resolver.ServiceNotAuthorizedException;
import org.eclipse.jgit.transport.resolver.ServiceNotEnabledException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: ts-jgit
 * @description:
 * @create: 2019-10-23 16:47
 **/
@SpringBootApplication
public class Application {

    private static int JETTY_PORT;

    @Value("${server.jetty.port}")
    public void setRepositoryPath(int jettyPort) {
        Application.JETTY_PORT = jettyPort;
    }

    private static Map<String, IJGitPlugin> pluginMap = new HashMap<>();

    public static IJGitPlugin getPlugin(String id){
        return pluginMap.get(id);
    }

    public static void main(String[] args) throws Exception {
        ApplicationContext context = SpringApplication.run(Application.class, args);
        Map<String, IJGitPlugin> myMap = context.getBeansOfType(IJGitPlugin.class);
        for (Map.Entry<String, IJGitPlugin> entry : myMap.entrySet()){
            IJGitPlugin plugin = entry.getValue();
            if (plugin.getId() != null){
                pluginMap.put(plugin.getId(), plugin);
            }
        }

        GitServlet gs = new GitServlet();
        gs.setRepositoryResolver(new RepositoryResolver<HttpServletRequest>() {
            @Override
            public Repository open(HttpServletRequest request, String repoName) throws RepositoryNotFoundException, ServiceNotAuthorizedException, ServiceNotEnabledException, ServiceMayNotContinueException {
                Repository repo = null;
                try {
                    File file = new File(JGitUtil.buildGitPath(repoName));
                    repo = FileRepositoryBuilder.create(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return repo;
            }
        });
        // start up the Servlet and start serving requests
        Server server = configureAndStartHttpServer(gs);

        // finally wait for the Server being stopped
        server.join();
    }

    private static Server configureAndStartHttpServer(GitServlet gs) throws Exception {
        Server server = new Server(JETTY_PORT);
        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);
        ServletHolder holder = new ServletHolder(gs);
        handler.addServletWithMapping(holder, "/*");
        server.start();
        return server;
    }

}
