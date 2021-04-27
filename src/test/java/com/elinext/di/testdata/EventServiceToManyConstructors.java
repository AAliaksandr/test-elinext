package com.elinext.di.testdata;

import com.elinext.di.Inject;

public class EventServiceToManyConstructors implements EventService{
    private EventDAO eventDAO;

    @Inject
    public EventServiceToManyConstructors(EventDAO eventDAO) {
        this.eventDAO = eventDAO;
    }

    @Inject
    public EventServiceToManyConstructors() {
    }
}
