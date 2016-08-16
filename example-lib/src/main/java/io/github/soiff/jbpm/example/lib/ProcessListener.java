package io.github.soiff.jbpm.example.lib;

import lombok.extern.slf4j.Slf4j;
import org.kie.api.event.process.*;

/**
 * Created by zhangh on 2016/8/16.
 *
 * @author : zhangh@dtdream.com
 * @version : 1.0.0
 * @since : 1.8
 */

@Slf4j
class ProcessListener implements ProcessEventListener {

    public void beforeProcessStarted(ProcessStartedEvent event) {
        log.info("+--------------+ {}", event);
    }

    public void afterProcessStarted(ProcessStartedEvent event) {
        log.info("+--------------+ {}", event);
    }

    public void beforeProcessCompleted(ProcessCompletedEvent event) {
        log.info("+--------------+ {}", event);
    }

    public void afterProcessCompleted(ProcessCompletedEvent event) {
        log.info("+--------------+ {}", event);
    }

    public void beforeNodeTriggered(ProcessNodeTriggeredEvent event) {
        log.info("+--------------+ {}", event);
    }

    public void afterNodeTriggered(ProcessNodeTriggeredEvent event) {
        log.info("+--------------+ {}", event);
    }

    public void beforeNodeLeft(ProcessNodeLeftEvent event) {
        log.info("+--------------+ {}", event);
    }

    public void afterNodeLeft(ProcessNodeLeftEvent event) {
        log.info("+--------------+ {}", event);
    }

    public void beforeVariableChanged(ProcessVariableChangedEvent event) {
        log.info("+--------------+ {}", event);
    }

    public void afterVariableChanged(ProcessVariableChangedEvent event) {
        log.info("+--------------+ {}", event);
    }
}

