package com.substring.foodies.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import lombok.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "foodie_restaurant")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Restaurant extends BaseAuditableEntity{

    @Id
    private String id;
    private String name;

    @Lob
    private String description;

    @ManyToMany
    @JoinTable(
            name = "restaurant_address", // join table name
            joinColumns = @JoinColumn(name = "restaurant_id"), // this entity's FK
            inverseJoinColumns = @JoinColumn(name = "address_id") // other entity's FK
    )
    private Set<Address> addresses = new HashSet<>();

    @Column(name = "open_time")
    private LocalTime openTime;

    @Column(name = "close_time")
    private LocalTime closeTime;

    private boolean isOpen=true;

    private boolean isActive=true;

    @DecimalMin(value = "0.0")
    @DecimalMax(value = "5.0")
    @Column(name = "rating_star")
    private Double rating;

    @ManyToMany
    @JoinTable(
            name = "restaurant_food",
            joinColumns = @JoinColumn(name = "restaurant_id"),
            inverseJoinColumns = @JoinColumn(name = "food_id")
    )
    private Set<FoodItems> foodItemsList = new HashSet<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cart> cartList = new ArrayList<>();

    private String banner;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
}
