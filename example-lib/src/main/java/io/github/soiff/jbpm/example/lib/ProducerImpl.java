package io.github.soiff.jbpm.example.lib;

import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.common.message.MessageConst;
import com.alibaba.rocketmq.common.protocol.ResponseCode;
import com.alibaba.rocketmq.remoting.exception.RemotingConnectException;
import com.alibaba.rocketmq.remoting.exception.RemotingTimeoutException;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by zhangh on 2016/8/16.
 *
 * @author : zhangh@dtdream.com
 * @version : 1.0.0
 * @since : 1.8
 */
@Slf4j
public class ProducerImpl<T> {
    private final DefaultMQProducer producer;
    private final AtomicBoolean started = new AtomicBoolean(false);
    private final AtomicBoolean closed  = new AtomicBoolean(false);

    public ProducerImpl(String producerGroup, String nameServer) {
        producer = new DefaultMQProducer(producerGroup);
        this.producer.setSendMsgTimeout(3000);
        this.producer.setInstanceName(MessageListener.INSTANCE);
        this.producer.setNamesrvAddr(nameServer);
        // 消息最大大小128K
        this.producer.setMaxMessageSize(1024 * 128);
    }

    public void start() {
        try {
            if (this.started.compareAndSet(false, true)) {
                this.producer.start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void shutdown() {
        if (this.closed.compareAndSet(false, true)) {
            this.producer.shutdown();
        }
    }

    private void checkServiceState() {
        switch (this.producer.getDefaultMQProducerImpl().getServiceState()) {
            case CREATE_JUST:
                throw new RuntimeException(String.format("You do not have start the producer, %s",
                    this.producer.getDefaultMQProducerImpl().getServiceState()));
            case SHUTDOWN_ALREADY:
                throw new RuntimeException(String.format("Your producer has been shut down, %s",
                    this.producer.getDefaultMQProducerImpl().getServiceState()));
            case START_FAILED:
                throw new RuntimeException(String.format("When you start your service throws an exception, %s",
                    this.producer.getDefaultMQProducerImpl().getServiceState()));
            case RUNNING:
                break;
            default:
                break;
        }
    }


    private void checkProducerException(Exception e, Message message) {
        if (e instanceof MQClientException) {
            //
            if (e.getCause() != null) {
                // 无法连接Broker
                if (e.getCause() instanceof RemotingConnectException) {
                    throw new RuntimeException(String.format("Connect broker failed, Topic: %s", message.getTopic()));
                }
                // 发送消息超时
                else if (e.getCause() instanceof RemotingTimeoutException) {
                    throw new RuntimeException(String.format("Send message to broker timeout, %dms, Topic: %s",
                        this.producer.getSendMsgTimeout(), message.getTopic()));
                }
                // Broker返回异常
                else if (e.getCause() instanceof MQBrokerException) {
                    MQBrokerException excep = (MQBrokerException) e.getCause();
                    throw new RuntimeException(String.format("Receive a broker exception, Topic: %s, %s",
                        message.getTopic(), excep.getErrorMessage()));
                }
            }
            // 纯客户端异常
            else {
                MQClientException excep = (MQClientException) e;
                if (-1 == excep.getResponseCode()) {
                    throw new RuntimeException( String.format("Topic does not exist, Topic: %s, %s",
                        message.getTopic(), excep.getErrorMessage()));
                } else if (ResponseCode.MESSAGE_ILLEGAL == excep.getResponseCode()) {
                    throw new RuntimeException(String.format("ONS Client check message exception, Topic: %s, %s",
                        message.getTopic(), excep.getErrorMessage()));
                }
            }
        }

        throw new RuntimeException("producer send exception", e);
    }

    public SendResult send(Message message) {
        this.checkServiceState();

        try {
            SendResult sendResult = this.producer.send(message);
            return sendResult;
        } catch (Exception e) {
            log.error(String.format("Send message Exception, %s", message), e);
            this.checkProducerException(e, message);
            return null;
        }
    }

    public static boolean isSystemProperty(String key){
        if (MessageConst.systemKeySet.contains(key)){
            return true;
        }
        return false;
    }

    public void sendOneway(Message message) {
        this.checkServiceState();
        try {
            this.producer.sendOneway(message);
        } catch (Exception e) {
            log.error(String.format("Send message oneway Exception, %s", message), e);
            this.checkProducerException(e, message);
        }
    }

    public boolean isStarted() {
        return started.get();
    }

    public boolean isClosed() {
        return closed.get();
    }
}

