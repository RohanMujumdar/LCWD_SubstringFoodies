package com.substring.foodies.entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {

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
    private List<Restaurant> restaurants = new ArrayList<>();
}
