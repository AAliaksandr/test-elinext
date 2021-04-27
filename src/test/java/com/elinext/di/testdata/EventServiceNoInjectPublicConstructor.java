package com.elinext.di.testdata;

public class EventServiceNoInjectPublicConstructor implements EventService{
    private EventDAO eventDAO;


    public EventServiceNoInjectPublicConstructor(EventDAO eventDAO) {
        this.eventDAO = eventDAO;
    }

        public EventServiceNoInjectPublicConstructor() {
    }
}
