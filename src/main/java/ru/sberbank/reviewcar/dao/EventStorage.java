package ru.sberbank.reviewcar.dao;

import ru.sberbank.reviewcar.model.Event;

import java.util.List;

public interface EventStorage {
    List<Event> getFeedByUserId(int userId);

    void createEvent(Event event);
}
