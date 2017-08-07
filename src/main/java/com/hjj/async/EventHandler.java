package com.hjj.async;

import java.util.List;

/**
 * Created by Administrator on 2017/8/8.
 */
public interface EventHandler {
    void doHandle(EventModel model);
    List<EventType> getSupportEventTypes();
}
