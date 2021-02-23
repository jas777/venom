package com.venomdevteam.venom.util.logger;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class LoggerAppender extends AppenderBase<ILoggingEvent> {

    private final ConcurrentMap<String, ILoggingEvent> eventMap
            = new ConcurrentHashMap<>();

    @Override
    protected void append(ILoggingEvent event) {
        eventMap.put("test: ", event);
    }

    public Map<String, ILoggingEvent> getEventMap() {
        return eventMap;
    }
}