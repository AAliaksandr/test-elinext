package com.elinext.di.testdata;

import com.elinext.di.Inject;

public class EventServiceImpl implements EventService{

    private final EventDAO eventDAO;

    @Inject
    public EventServiceImpl(EventDAO eventDAO) {
        this.eventDAO = eventDAO;
    }

}
