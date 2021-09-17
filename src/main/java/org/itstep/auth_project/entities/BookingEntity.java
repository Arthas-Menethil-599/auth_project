package org.itstep.auth_project.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BookingEntity extends BookingBaseEntity {

    private String name;

    private String description;

    private Integer rooms;

    private Integer price;
}
