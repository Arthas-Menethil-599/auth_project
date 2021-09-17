package org.itstep.auth_project.services;

import org.itstep.auth_project.entities.BookingEntity;
import java.util.List;

public interface SearchService {
    List<BookingEntity> searchBookingEntities(Integer page, int size, String name, String description, Integer roomsFrom, Integer roomsTo, Integer priceFrom, Integer priceTo);

    int countBookingEntities(String name, String description, Integer roomsFrom, Integer roomsTo, Integer priceFrom, Integer priceTo);
}
