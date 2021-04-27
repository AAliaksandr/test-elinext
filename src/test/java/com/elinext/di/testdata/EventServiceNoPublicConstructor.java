package com.elinext.di.testdata;

import com.elinext.di.Inject;

public class EventServiceNoPublicConstructor implements EventService {
    private final EventDAO eventDAO;

    @Inject
     EventServiceNoPublicConstructor(EventDAO eventDAO) {
        this.eventDAO = eventDAO;
    }
}
