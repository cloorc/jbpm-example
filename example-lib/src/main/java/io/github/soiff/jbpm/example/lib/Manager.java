package io.github.soiff.jbpm.example.lib;

import org.kie.api.KieServices;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.*;
import org.kie.api.task.UserGroupCallback;
import org.kie.server.api.marshalling.MarshallingFormat;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangh on 2016/8/16.
 *
 * @author : zhangh@dtdream.com
 * @version : 1.0.0
 * @since : 1.8
 */
public class Manager {
    private static final String URL = "http://192.168.103.104:8080/kie-server/services/rest/server";
    private static final String USER = "kieserver";
    private static final String PASSWORD = "kieserver1!";
    private static final MarshallingFormat FORMAT = MarshallingFormat.JSON;

    private static KieServicesConfiguration conf;
    private static KieServicesClient kieServicesClient;

    private static RuntimeManager manager;
    private static RuntimeEngine runtime;
    private static KieSession ksession;

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public synchronized void run() {
                if (null != manager && null != runtime)
                    manager.disposeRuntimeEngine(runtime);
            }
        }));
    }

    public static final RuntimeManager getRuntimeManager() {
        if (null != manager)
            return manager;
        synchronized (manager) {
            RuntimeEnvironment environment = RuntimeEnvironmentBuilder.Factory.get().newDefaultBuilder()
                .userGroupCallback(new UserGroupCallback() {
                    public List<String> getGroupsForUser(String userId, List<String> groupIds, List<String> allExistingGroupIds) {
                        List<String> result = new ArrayList<String>();
                        if ("sales-rep".equals(userId)) {
                            result.add("sales");
                        } else if ("john".equals(userId)) {
                            result.add("PM");
                        }
                        return result;
                    }

                    public boolean existsUser(String arg0) {
                        return true;
                    }

                    public boolean existsGroup(String arg0) {
                        return true;
                    }
                })
                .addAsset(KieServices.Factory.get().getResources().newClassPathResource("humantask/HumanTask.bpmn"), ResourceType.BPMN2)
                .get();
            return manager = RuntimeManagerFactory.Factory.get().newSingletonRuntimeManager(environment);
        }
    }

    public static final RuntimeEngine getRuntimeEngine() {
        if (null != runtime)
            return runtime;
        synchronized (runtime) {
            return runtime = getRuntimeManager().getRuntimeEngine(null);
        }
    }

    public static final KieSession getKieSession() {
        if (null != ksession)
            return ksession;
        synchronized (ksession) {
            return ksession = getRuntimeEngine().getKieSession();
        }
    }
}
