package org.itstep.auth_project.services;

import org.itstep.auth_project.entities.BookingEntity;
import org.itstep.auth_project.repositories.BookingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {

    private final BookingRepository bookingRepository;

    public SearchServiceImpl(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public List<BookingEntity> searchBookingEntities(Integer page, int size, String name, String description, Integer roomsFrom, Integer roomsTo, Integer priceFrom, Integer priceTo) {
        Pageable pageable = PageRequest.of(page, size);
        Specification specification = (Specification<BookingEntity>) (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get("name")),"%"+name.toUpperCase()+"%");
        if(!description.equals("")) {
            specification = specification.and((Specification<BookingEntity>) (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get("description")),"%"+description.toUpperCase()+"%"));
        }
        if(roomsFrom != 0) {
            specification = specification.and((Specification<BookingEntity>) (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("rooms"), roomsFrom));
        }
        if(roomsTo != 0) {
            specification = specification.and((Specification<BookingEntity>) (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("rooms"), roomsTo));
        }
        if(priceFrom != 0) {
            specification = specification.and((Specification<BookingEntity>) (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("price"),priceFrom));
        }
        if(priceTo != 0) {
            specification = specification.and((Specification<BookingEntity>) (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("price"), priceTo));
        }

        Page<BookingEntity> bookingEntityPage = bookingRepository.findAll(specification, pageable);
        return bookingEntityPage.getContent();
    }

    @Override
    public int countBookingEntities(String name, String description, Integer roomsFrom, Integer roomsTo, Integer priceFrom, Integer priceTo) {
        Specification specification = (Specification<BookingEntity>) (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get("name")),"%"+name.toUpperCase()+"%");
        if(!description.equals("")) {
            specification = specification.and((Specification<BookingEntity>) (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get("description")),"%"+description.toUpperCase()+"%"));
        }
        if(roomsFrom != 0) {
            specification = specification.and((Specification<BookingEntity>) (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("rooms"), roomsFrom));
        }
        if(roomsTo != 0) {
            specification = specification.and((Specification<BookingEntity>) (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("rooms"), roomsTo));
        }
        if(priceFrom != 0) {
            specification = specification.and((Specification<BookingEntity>) (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("price"),priceFrom));
        }
        if(priceTo != 0) {
            specification = specification.and((Specification<BookingEntity>) (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("price"), priceTo));
        }
        return (int) bookingRepository.count(specification);
    }
}
