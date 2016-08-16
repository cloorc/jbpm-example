package io.github.soiff.jbpm.example.start;

import io.github.soiff.jbpm.example.lib.Manager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangh on 2016/8/16.
 *
 * @author : zhangh@dtdream.com
 * @version : 1.0.0
 * @since : 1.8
 */
public class Starter {

    public static void main(String[] args) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", "krisv");
        params.put("description", "Need a new laptop computer");
        Manager.getKieSession().startProcess("com.sample.humantask", params);
    }
}
