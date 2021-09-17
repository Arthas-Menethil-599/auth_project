package org.itstep.auth_project.services;

import org.itstep.auth_project.entities.BookingEntity;
import java.util.List;

public interface BookingService {

    BookingEntity getBookingEntity(Long id);

    List<BookingEntity> getBookingEntities();

    BookingEntity addBookingEntity(BookingEntity bookingEntity);

    BookingEntity saveBookingEntity(BookingEntity bookingEntity);

    void deleteBookingEntity(BookingEntity bookingEntity);
}
