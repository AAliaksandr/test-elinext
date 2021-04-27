package com.elinext.di.testdata;

import com.elinext.di.Inject;

public class EventServiceCyclicException implements EventService{

    //    private final com.elinext.di.testdata.EventDAO eventDAO;
//
//    @com.elinext.di.Inject
//    public com.elinext.di.testdata.EventServiceImpl(com.elinext.di.testdata.EventDAO eventDAO) {
//        this.eventDAO = eventDAO;
//    }
    private EventService eventService;
    @Inject
    public EventServiceCyclicException(EventService eventService) {
        this.eventService = eventService;
    }
}