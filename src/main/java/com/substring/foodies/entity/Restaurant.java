package com.substring.foodies.entity;


import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "foodie_restaurant")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Restaurant {

    @Id
    private String id;
    private String name;

    @Lob
    private String description;
    private String address;

    @Column(name = "open_time")
    private LocalTime openTime;

    @Column(name = "close_time")
    private LocalTime closeTime;
    private boolean isOpen=true;

    private String banner;

//    @Column(name = "created_date")
    private LocalDateTime createdDateTime;

    @ManyToOne
    private User user;

}
