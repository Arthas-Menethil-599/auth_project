package org.itstep.auth_project.services;

import org.itstep.auth_project.entities.BookingEntity;
import org.itstep.auth_project.repositories.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    public BookingServiceImpl(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public BookingEntity getBookingEntity(Long id) {
        return bookingRepository.findByDeletedAtNullAndId(id);
    }

    @Override
    public List<BookingEntity> getBookingEntities() {
        return bookingRepository.findAllByDeletedAtNull();
    }

    @Override
    public BookingEntity addBookingEntity(BookingEntity bookingEntity) {
        return bookingRepository.save(bookingEntity);
    }

    @Override
    public BookingEntity saveBookingEntity(BookingEntity bookingEntity) {
        return bookingRepository.save(bookingEntity);
    }

    @Override
    public void deleteBookingEntity(BookingEntity bookingEntity) {
        bookingEntity.setDeletedAt(new Date());
        bookingRepository.save(bookingEntity);
    }
}
