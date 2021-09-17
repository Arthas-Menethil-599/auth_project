package org.itstep.auth_project.repositories;

import org.itstep.auth_project.entities.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Long>, JpaSpecificationExecutor<BookingEntity> {

    BookingEntity findByDeletedAtNullAndId(Long id);
    List<BookingEntity> findAllByDeletedAtNull();
}
