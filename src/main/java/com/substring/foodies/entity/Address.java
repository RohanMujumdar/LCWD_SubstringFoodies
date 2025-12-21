package com.substring.foodies.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address extends BaseAuditableEntity{

    @Id
    private String id;

    @Column(columnDefinition = "TEXT")
    private String addressLine;
    private String city;
    private String state;
    private String pincode;
    private String country;

    @OneToOne
    private User user;

    @ManyToMany(mappedBy = "addresses")
    private Set<Restaurant> restaurants = new HashSet<>();
}
