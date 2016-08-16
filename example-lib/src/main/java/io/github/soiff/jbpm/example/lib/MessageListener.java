package io.github.soiff.jbpm.example.lib;


import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.common.message.MessageExt;

/**
 * @Description:
 * @Author: luogb
 * @Date: 2016/1/13
 * @Author: <a href="mailto:luogb@dtdream.com">luogb</a>
 */
public interface MessageListener {

    public static final String INSTANCE = "jbpm-instance";
    public static final String TOPIC = "jbpm-topic";
    public static final String FILTER = "jbpm-filter";

    ConsumeConcurrentlyStatus consume(final MessageExt message);
}
