package io.github.soiff.jbpm.example.lib;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.alibaba.rocketmq.common.protocol.heartbeat.MessageModel;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by zhangh on 2016/8/16.
 *
 * @author : zhangh@dtdream.com
 * @version : 1.0.0
 * @since : 1.8
 */
public class ConsumerImpl {

    private final DefaultMQPushConsumer consumer;
    private final MessageListener subscriber;

    private final AtomicBoolean started = new AtomicBoolean(false);
    private final AtomicBoolean closed = new AtomicBoolean(false);

    public ConsumerImpl(String consumerGroup, String nameServer, MessageListener subscriber) throws MQClientException {
        this.subscriber = subscriber;
        this.consumer = new DefaultMQPushConsumer(consumerGroup);
        this.consumer.setMessageModel(MessageModel.CLUSTERING);
        this.consumer.setInstanceName(MessageListener.INSTANCE);
        this.consumer.setNamesrvAddr(nameServer);
        this.consumer.setConsumeThreadMin(1);
        this.consumer.setConsumeThreadMax(3);
        // Result in only one element in consumeMessage()
        this.consumer.setConsumeMessageBatchMaxSize(1);
        this.consumer.subscribe(MessageListener.TOPIC, MessageListener.FILTER);
    }

    public void start() {
        this.consumer.registerMessageListener(new MessageListenerImpl());
        try {
            if (this.started.compareAndSet(false, true)) {
                this.consumer.start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void shutdown() {
        if (this.closed.compareAndSet(false, true)) {
            this.consumer.shutdown();
        }
    }

    class MessageListenerImpl implements MessageListenerConcurrently {
        public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgsRMQList, ConsumeConcurrentlyContext contextRMQ) {
            MessageExt message = msgsRMQList.get(0);
            if (null != message)
                return subscriber.consume(message);
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
    }

    public void unsubscribe() {
        this.consumer.unsubscribe(MessageListener.TOPIC);
    }

    public void setConsumeFromWhere(ConsumeFromWhere consumeFromWhere){
        this.consumer.setConsumeFromWhere(consumeFromWhere);
    }

    public boolean isStarted() {
        return started.get();
    }

    public boolean isClosed() {
        return closed.get();
    }
}
